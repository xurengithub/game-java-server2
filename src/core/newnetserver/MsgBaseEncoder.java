package core.newnetserver;

import core.utils.ArrayUtils;
import core.utils.ServNetUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import proto.MsgBase;

public class MsgBaseEncoder implements ProtocolEncoder {
    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        MsgBase s = null;
        if(message instanceof MsgBase){
            s = (MsgBase)message;
        }

        if(s != null){

            byte[] nameBytes = MsgBase.EncodeName(s);
            byte[] bodyBytes = MsgBase.Encode(s);
            int len = nameBytes.length + bodyBytes.length;
            byte[] sendBytes = new byte[4+len];

            byte[] allLenBytes = ServNetUtils.intToByteArray(len);
            for (int i = 0; i < allLenBytes.length;i++){
                sendBytes[i] = allLenBytes[i];
            }
            ServNetUtils.copy(nameBytes, 0 , sendBytes, 4,nameBytes.length);
            ServNetUtils.copy(bodyBytes, 0,sendBytes, 4+nameBytes.length, bodyBytes.length);
//            byte[] msgBytes = s.getBytes();
//            byte[] msgLenBytes = ServNetUtils.intToByteArray(msgBytes.length);
//            byte[] sendBytes = ArrayUtils.Connect(msgLenBytes, msgBytes);

//            System.out.println(new String(sendBytes));
            IoBuffer ioBuffer = IoBuffer.allocate(sendBytes.length);
            ioBuffer.setAutoExpand(true);
            ioBuffer.put(sendBytes);
            ioBuffer.flip();
            out.write(ioBuffer);

        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
