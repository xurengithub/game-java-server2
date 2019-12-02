package core.newnetserver;

import core.newnetserver.protocol.SocketModel;
import game.message.MessageManager;
import game.playerino.Player;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proto.MsgBase;
import proto.MsgOutGame;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MyServerHandler extends IoHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(MyServerHandler.class);
    public static Set<IoSession> sessions = Collections.synchronizedSet(new HashSet<IoSession>());
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        this.logger.debug("client connected");
        sessions.add(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        sessions.remove(session);
        Player player = (Player) session.getAttribute("player");
        if(player!=null){
            player.update();
            MsgOutGame msg = new MsgOutGame();
            this.logger.debug("sessionClosed2"+player.playerId+"session关闭并广播");
            msg.setPlayerId(player.playerId);
            player.area.broadcast(msg);
            //清空player 让gc回收
            player.close();
        }
        session.closeNow();
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

        Player player = (Player) session.getAttribute("player");
        if(player!=null){
            player.update();
            MsgOutGame msg = new MsgOutGame();
            this.logger.debug("用户"+player.playerId+"闲置下线并广播");
            msg.setPlayerId(player.playerId);
            player.area.broadcast(msg);
            //清空player 让gc回收
            player.close();
        }
        session.closeNow();
//        System.out.println("[Server] IDLE " + session.getRemoteAddress() + session.getIdleCount(status));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);

        MsgBase msgBase = (MsgBase) message;

        MessageManager.msgTransfer(session, msgBase);
    }


}
