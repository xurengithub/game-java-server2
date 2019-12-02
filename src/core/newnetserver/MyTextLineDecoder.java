package core.newnetserver;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyTextLineDecoder implements ProtocolDecoder {
    @Override
    public void decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
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

                String str = new String(bytes);
                out.write(str);
            }

        }

    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
