package core.newnetserver;

import core.newnetserver.protocol.SocketModel;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class SocketModelDecoder extends CumulativeProtocolDecoder {
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
                SocketModel socketModel = new SocketModel(bytes);
                out.write(socketModel);
                return true;
            }

        }
        in.position(startPosition);
        return false;
    }
}
