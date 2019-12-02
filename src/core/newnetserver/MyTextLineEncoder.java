package core.newnetserver;

import core.utils.ArrayUtils;
import core.utils.ServNetUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class MyTextLineEncoder implements ProtocolEncoder {
    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        String s = null;
        if(message instanceof String){
            s = (String)message;
        }

        if(s != null){

            byte[] msgBytes = s.getBytes();
            byte[] msgLenBytes = ServNetUtils.intToByteArray(msgBytes.length);
            byte[] sendBytes = ArrayUtils.Connect(msgLenBytes, msgBytes);

            IoBuffer ioBuffer = IoBuffer.allocate(sendBytes.length);
            ioBuffer.setAutoExpand(true);
            ioBuffer.put(sendBytes);
            ioBuffer.flip();
            out.write(ioBuffer);
//            CharsetEncoder charsetEncoder = (CharsetEncoder) ioSession.getAttribute("encoder");
//            if(charsetEncoder == null){
//                charsetEncoder = Charset.defaultCharset().newEncoder();
//                ioSession.setAttribute("encoder",charsetEncoder);
//            }
//            IoBuffer ioBuffer = IoBuffer.allocate(s.length());
//            ioBuffer.setAutoExpand(true);
//            ioBuffer.putString(s,charsetEncoder);//Charset.defaultCharset().newEncoder());
//
//            ioBuffer.flip();
//            out.write(ioBuffer);
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
