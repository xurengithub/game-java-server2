package core.newnetserver;

import core.utils.ArrayUtils;
import core.utils.ServNetUtils;
import net.sf.json.JSONObject;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import proto.MsgBase;

public class MyDecoder extends CumulativeProtocolDecoder {



    /**

     * 这种方法的返回值是重点：

     * 1、当内容刚好时，返回false，告知父类接收下一批内容

     * 2、内容不够时须要下一批发过来的内容，此时返回false，这样父类 CumulativeProtocolDecoder

     *   会将内容放进IoSession中，等下次来数据后就自己主动拼装再交给本类的doDecode

     * 3、当内容多时，返回true，由于须要再将本批数据进行读取。父类会将剩余的数据再次推送本

     * 类的doDecode

     */

    public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if(in.remaining() > 0){//有数据时。读取前8字节推断消息长度

            byte [] sizeBytes = new byte[8];

            in.mark();//标记当前位置。以便reset

//由于我的前数据包的长度是保存在第4-8字节中，

            in.get(sizeBytes,0,4);//读取4字节

            //DataTypeChangeHelper是自己写的一个byte[]转int的一个工具类

            int size = ServNetUtils.byteArrayToInt(sizeBytes);

//            System.out.println(in.remaining());
//            System.out.println(in.position());
            in.reset();
//            System.out.println(in.remaining());
//            System.out.println(in.position());
            if(size > in.remaining()){//假设消息内容不够，则重置。相当于不读取size

                return false;//父类接收新数据，以拼凑成完整数据

            } else{

                byte[] bytes = new byte[size+4];

                in.get(bytes, 0, size+4);
                //把字节转换为Java对象的工具类

                IoBuffer ioBuffer = IoBuffer.wrap(bytes);

                int startPosition = ioBuffer.position();
                while (ioBuffer.hasRemaining()) {
                    byte b = ioBuffer.get();
//                    System.out.println(b);


                }
                int currPostion = ioBuffer.position();
                int limit = ioBuffer.limit();

                ioBuffer.position(startPosition);
                ioBuffer.limit(currPostion);
                IoBuffer slice = ioBuffer.slice();
                ioBuffer.position(currPostion);
                ioBuffer.limit(limit);
                bytes = new byte[slice.limit()];
                slice.get(bytes);
//                ioBuffer.get(bytes);

                byte[] nameBytesLen = new byte[4];
                ServNetUtils.copy(bytes, nameBytesLen, 4);
                int int32 = ServNetUtils.byteArrayToInt(nameBytesLen);
                byte[] nameBytesLen2 = new byte[4];
                ServNetUtils.copy(bytes, 4,nameBytesLen2, 0,4);
                int int162 = ServNetUtils.byteArrayToInt(nameBytesLen2);
                byte[] protoNameBytes = new byte[int162];
                ServNetUtils.copy(bytes,8,protoNameBytes,0,int162);
                String protoName = new String(protoNameBytes);
                int bodylen = bytes.length-8-int162;
                byte[] protoBodyBytes = new byte[bodylen];
                ServNetUtils.copy(bytes, 8+int162,protoBodyBytes,0,bodylen);
                String jsonStr = new String(protoBodyBytes);
                System.out.println(jsonStr);
                JSONObject jsonObject = JSONObject.fromObject(jsonStr);
                MsgBase msgBase = (MsgBase) JSONObject.toBean(jsonObject, getclass(protoName));
//                byte[] nameBytesLen2 = new byte[4];
//                ServNetUtils.copy(bytes, 0,nameBytesLen2, 0,4);
//                int int162 = ServNetUtils.byteArrayToInt(nameBytesLen2);
//                byte[] protoNameBytes = new byte[int162];
//                ServNetUtils.copy(bytes,4,protoNameBytes,0,int162);
//                String protoName = new String(protoNameBytes);
//                int bodylen = bytes.length-4-int162;
//                byte[] protoBodyBytes = new byte[bodylen];
//                ServNetUtils.copy(bytes, 4+int162,protoBodyBytes,0,bodylen);
//                String jsonStr = new String(protoBodyBytes);
//                JSONObject jsonObject = JSONObject.fromObject(jsonStr);
//                MsgBase msgBase = (MsgBase) JSONObject.toBean(jsonObject, getclass(protoName));

                out.write(msgBase);

                if(in.remaining() > 0){//假设读取内容后还粘了包，就让父类再重读  一次。进行下一次解析

                    return true;

                }

            }

        }

        return false;//处理成功，让父类进行接收下个包

    }

    public static Class getclass(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException//className是类名
    {
        Class obj=Class.forName("proto."+className); //以String类型的className实例化类

        return obj;
    }
//    getter();
//
//    Setter();

}
