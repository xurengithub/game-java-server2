package core.newnetserver;

//import com.sun.org.apache.xpath.internal.operations.String;
import core.newnetserver.protocol.SocketModel;
import core.utils.ArrayUtils;
import core.utils.ServNetUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import proto.MsgBase;
import proto.MsgLogin;

public class MsgBaseDecoder extends CumulativeProtocolDecoder {
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        int startPosition = in.position();
        while (in.hasRemaining()){
            byte b = in.get();
            if(b == '\n'){
                int currPostion = in.position();
                int limit = in.limit();

                in.position(startPosition);
                in.limit(currPostion);
                IoBuffer slice = in.slice();
                in.position(currPostion);
                in.limit(limit);
                byte[] bytes = new byte[slice.limit()];
                slice.get(bytes);

                String str= new String(bytes);
                System.out.println(str);

                byte[] nameBytesLen = new byte[4];
                ServNetUtils.copy(bytes, nameBytesLen, 4);
                int int32 = ServNetUtils.byteArrayToInt(nameBytesLen);
                byte[] nameBytesLen2 = new byte[4];
                ServNetUtils.copy(bytes, 4,nameBytesLen2, 0,4);
                int int162 = ServNetUtils.byteArrayToInt(nameBytesLen2);
                byte[] protoNameBytes = new byte[int162];
                ServNetUtils.copy(bytes,8,protoNameBytes,0,int162);
                String protoName = new String(protoNameBytes);
                int bodylen = bytes.length-8-int162-1;
                byte[] protoBodyBytes = new byte[bodylen];
                ServNetUtils.copy(bytes, 8+int162,protoBodyBytes,0,bodylen);
                String jsonStr = new String(protoBodyBytes);
                JSONObject jsonObject = JSONObject.fromObject(jsonStr);
                MsgBase msgBase = (MsgBase) JSONObject.toBean(jsonObject, getclass(protoName));

                out.write(msgBase);
                return true;
            }

        }
        in.position(startPosition);
        return false;
    }








        public static Class getclass(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException//className是类名
        {
            Class obj=Class.forName("proto."+className); //以String类型的className实例化类

            return obj;
        }



}
