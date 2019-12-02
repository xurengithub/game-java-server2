package core.newnetserver;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyTextLineFactroy implements ProtocolCodecFactory {
    private MyTextLineDecoder decoder;
    private MyTextLineEncoder encoder;
    private MyTextLineCumulativeDecoder cumulativeDecoder;
    private SocketModelDecoder socketModelDecoder;

    private MsgBaseEncoder msgBaseEncoder;
    private MsgBaseDecoder msgBaseDecoder;
    private MyDecoder myDecoder;
    public MyTextLineFactroy(){
        decoder = new MyTextLineDecoder();
        encoder = new MyTextLineEncoder();
        cumulativeDecoder = new MyTextLineCumulativeDecoder();
        socketModelDecoder = new SocketModelDecoder();
        msgBaseDecoder = new MsgBaseDecoder();
        msgBaseEncoder = new MsgBaseEncoder();
        myDecoder = new MyDecoder();
    }
    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return msgBaseEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return myDecoder;
    }
}
