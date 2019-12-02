package game.message.handler;

import core.newnetserver.protocol.SocketModel;
import org.apache.mina.core.session.IoSession;
import proto.MsgBase;

public interface AbstractHandler {
    public void handle(IoSession session, MsgBase msgBase);
}
