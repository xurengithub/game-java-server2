package game.message.handler;

import core.newnetserver.protocol.SocketModel;
import game.playerino.Player;
import game.playerino.player.PlayerDataEntity;
import game.playerino.player.PlayerEntity;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;
import proto.MsgAcAttack;
import proto.MsgBase;
import proto.MsgSyncPlayer;

@Component
public class SynHandler implements AbstractHandler{

    @Override
    public void handle(IoSession session, MsgBase msgBase) {

        if(msgBase instanceof MsgSyncPlayer){
            syncPlayer(session, (MsgSyncPlayer) msgBase);
        }
        if(msgBase instanceof MsgAcAttack){
            acAttack(session, (MsgAcAttack) msgBase);
        }
//        if(session.getAttribute("user") == null){
//            session.write("you dont login");
//            return;
//        }
//        if(msgBase instanceof MsgSyncPlayer){
//            msgSyncPlayer(session, msgBase);
//        }
    }

//    public void msgSyncPlayer(IoSession session, MsgBase msgBase){
//        Player player = (Player) session.getAttribute("player");
//        if(player == null){
//            return;
//        }
//        MsgSyncPlayer positionMsg = (MsgSyncPlayer) msgBase;
//        //判断是否作弊
//        PlayerDataEntity pos = player.playerDataEntity;
//        if(Math.abs(pos.x - positionMsg.x) > 5 ||
//                Math.abs(pos.y - positionMsg.y) > 5 ||
//                Math.abs(pos.z - positionMsg.z) > 5){
//            System.out.println("疑似作弊");
//            return;
//        }
//        pos.x = positionMsg.x;
//        pos.y = positionMsg.y;
//        pos.z = positionMsg.z;
//        positionMsg.playerId = player.playerId;
//
//        player.area.broadcast(positionMsg);
//    }

    private void syncPlayer(IoSession session, MsgSyncPlayer msg){

        Player player = (Player) session.getAttribute("player");

        player.playerEntity.setX(msg.getX());
        player.playerEntity.setY(msg.getY());
        player.playerEntity.setZ(msg.getZ());
        player.playerEntity.setEx(msg.getEx());
        player.playerEntity.setEy(msg.getEy());
        player.playerEntity.setEz(msg.getEz());
        msg.setPlayerId(player.playerId);
        player.area.broadcast(msg);
    }

    private void acAttack(IoSession session, MsgAcAttack msg){
        Player player = (Player)session.getAttribute("player");
        msg.setPlayerId(player.playerId);
        player.area.broadcast(msg);
    }
}
