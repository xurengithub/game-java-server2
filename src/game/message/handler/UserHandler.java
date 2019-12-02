package game.message.handler;

import core.newnetserver.NetManager;
import core.newnetserver.protocol.SocketModel;
import game.common.struct.constants.MsgType;
import game.message.MessageManager;
import game.playerino.Player;
import game.playerino.equipment.ItemsManager;
import game.playerino.player.PlayerDao;
import game.playerino.player.PlayerEntity;
import game.playerino.player.PlayerManager;
import game.scene.Area;
import game.user.User;
import game.user.UserManager;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import proto.*;
import sun.nio.ch.Net;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Component
public class UserHandler implements AbstractHandler {
    @Autowired
    private UserManager userManager;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private ItemsManager itemsManager;

    private Logger logger = LoggerFactory.getLogger(UserHandler.class);

    @Override
    public void handle(IoSession session, MsgBase msgBase) {

        if(msgBase instanceof MsgLogin){
            login(session, (MsgLogin) msgBase);
        }
        if(msgBase instanceof MsgRegister){
            register(session, (MsgRegister)msgBase);
        }
        if(msgBase instanceof MsgCreatePlayer){
            createPlayer(session, (MsgCreatePlayer)msgBase);
        }
        if(msgBase instanceof MsgPing){
            ping(session, (MsgPing)msgBase);
        }

//        switch (socketModel.command){
//            case MsgType.USER_REGISTER:register(session, socketModel.getMessage());
//            break;
//            case MsgType.USER_LOGIN:login(session, socketModel.getMessage());
//            break;
//            case MsgType.USER_LOGOUT:logout(session, socketModel.getMessage());
//            break;
//            case MsgType.USER_CREATE_PLAYER:createPlayer(session, socketModel.getMessage());
//            break;
//        }
    }

    private void register(IoSession session,MsgRegister msgRegister){
        boolean isSucc = userManager.register(msgRegister.getId(), msgRegister.getPw());
        if(!isSucc){
            msgRegister.setResult(1);
            NetManager.send(session, msgRegister);
            return;
        }

        NetManager.send(session, msgRegister);

    }

    private void login(IoSession session,MsgLogin msgLogin){
//        if(session.getAttribute("user") != null){
//            msgLogin.result = -1;
//        }

        long uid = userManager.login(msgLogin.getId(), msgLogin.getPw());
        if(uid == -1){
            msgLogin.setResult(1);
            NetManager.send(session,msgLogin);
            return;
        }
        this.logger.debug("user {} login succ!",uid);
        List<PlayerEntity> players = playerDao.findPlayersInfoByUserId(uid);
        PlayerSimpleInfo[] playerSimpleInfos = new PlayerSimpleInfo[players.size()];
        for(int i =0;i<players.size();i++){
            PlayerSimpleInfo playerSimpleInfo =new PlayerSimpleInfo();
            playerSimpleInfo.playerId = players.get(i).getPlayerId();
            playerSimpleInfos[i] = playerSimpleInfo;
        }
        msgLogin.setPlayerSimpleInfos(playerSimpleInfos);

        session.setAttribute("userId", uid);
        NetManager.send(session,msgLogin);
    }

//    private void logout(IoSession session,Object message){
//        User user = (User) session.getAttribute("user");
//        if(user == null){
//            session.write("fail");
//            return;
//        }
//        this.logger.debug("user {} logou succ",user.uid);
//        session.removeAttribute("user", user);
//        user.unUse();
//        session.closeNow();
//
//    }

    private void createPlayer(IoSession session, MsgCreatePlayer msgCreatePlayer){
        if(session.getAttribute("userId") == null){
            msgCreatePlayer.setResult(1);
            NetManager.send(session, msgCreatePlayer);
            return;
        }
        long userId = (long)session.getAttribute("userId");
        //测试数据
        Random random = new Random();
        msgCreatePlayer.setPlayerName("xuren22"+random.nextInt(99999));
        PlayerEntity p = playerDao.findByPlayerName(msgCreatePlayer.getPlayerName());
        if(p != null){
            msgCreatePlayer.setResult(1);
            NetManager.send(session, msgCreatePlayer);
            return;
        }

        //创建新用户信息
        Player player = new Player(session, playerDao, itemsManager);

        PlayerEntity playerEntity = new PlayerEntity();

        playerEntity.setUserId(userId);
        playerEntity.setCoin(100);
        playerEntity.setGem(0);
        playerEntity.setExp(0);
        playerEntity.setLevel(0);
        playerEntity.setPlayerName(msgCreatePlayer.getPlayerName());
        playerEntity.setScene("noviceCun");
        playerEntity.setAreaId(0);
        playerEntity.setX(100);
        playerEntity.setY(100);
        playerEntity.setZ(100);
        playerEntity.setMp(100);
        playerEntity.setHp(100);
        playerDao.save(playerEntity);

        PlayerEntity p2 = playerDao.findByPlayerName(msgCreatePlayer.getPlayerName());
        player.playerId = p2.getPlayerId();
        player.playerEntity = p2;
        player.areaId = p2.getAreaId();
        player.scene = p2.getScene();

        PlayerManager.addPlayer(player.playerId,player);

        //获取区域信息并添加入area
        Area area = NetManager.scenes.get(p2.getScene())[p2.getAreaId()];
        area.addPlayer(p2.getPlayerId());

        msgCreatePlayer.setPlayerEntity(p2);
        msgCreatePlayer.setPlayerId(p2.getPlayerId());

        session.setAttribute("player", player);
        NetManager.send(session, msgCreatePlayer);


    }

    private void ping(IoSession session, MsgPing msgPing){
        MsgPong msgPong = new MsgPong();
        NetManager.send(session, msgPong);
    }

}
