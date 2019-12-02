package game.message.handler;

import core.newnetserver.MyServerHandler;
import core.newnetserver.NetManager;
import core.newnetserver.protocol.SocketModel;
import game.common.struct.constants.MsgType;
import game.metadata.ItemData;
import game.playerino.Player;
import game.playerino.equipment.ItemEntity;
import game.playerino.equipment.ItemsManager;
//import game.playerino.player.Player;
import game.playerino.player.PlayerDao;
import game.playerino.player.PlayerEntity;
import game.playerino.player.PlayerManager;
import game.scene.Area;
import game.user.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import proto.*;
import sun.nio.ch.Net;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerHandler implements AbstractHandler {
    private Logger logger = LoggerFactory.getLogger(PlayerHandler.class);

    @Autowired
    private ItemsManager itemsManager;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ItemData itemData;

    @Override
    public void handle(IoSession session, MsgBase msgBase) {
        if(msgBase instanceof MsgEnterGame){
            enter(session, (MsgEnterGame) msgBase);
        }
        if(msgBase instanceof MsgBuyItem){
            buy(session, (MsgBuyItem) msgBase);
        }
        if(msgBase instanceof MsgOutGame){
            out(session, (MsgOutGame) msgBase);
        }

    }

    /**
     * 买item
     * @param session
     * @param msgBuyItem
     */
    private void buy(IoSession session, MsgBuyItem msgBuyItem){

        Player player = (Player) session.getAttribute("player");

        JSONObject item = itemData.findJsonObjById(msgBuyItem.getItemId());

        PlayerEntity playerEntity = player.playerEntity;
        List<ItemEntity> itemEntityList = player.itemEntityList;
        long coinNum = playerEntity.getCoin();

        int price = item.getInt("buyPrice");
        if(coinNum < price){
            msgBuyItem.setResult(1);
            NetManager.send(session, msgBuyItem);
            return;
        }

        //是否可以叠加
        int stackMax = item.getInt("stackMax");
        ItemEntity itemEntity = null;
        boolean isAdd = false;
        for(int i = 0;i<itemEntityList.size();i++){
            itemEntity = itemEntityList.get(i);
            if(itemEntity.getNum() < stackMax){
                itemEntity.setNum(itemEntity.getNum()+1);
//                itemDao.update(itemEntity);
                isAdd = true;
                break;
            }
        }
        //新的一栏位
        if(!isAdd){

            if(itemEntityList.size()>=36){
                msgBuyItem.setResult(1);
                NetManager.send(session, msgBuyItem);
                return;
            }

            itemEntity = new ItemEntity();
            itemEntity.setPlayerId(player.playerId);
            itemEntity.setNum(1);
            itemEntity.setItemId(msgBuyItem.getItemId());
            itemEntity.setAttribute(getAttributeJsonStr(item));
            itemEntityList.add(itemEntity);
        }

        coinNum -= price;
        playerEntity.setCoin(coinNum);

        NetManager.send(session, msgBuyItem);


    }

    /**
     * 玩家退出游戏
     * @param session
     * @param msgOutGame
     */
    private void out(IoSession session, MsgOutGame msgOutGame) {
        Player player = (Player) session.getAttribute("player");
        //保存玩家数据
        player.update();
        player.close();
        msgOutGame.setPlayerId(player.playerId);
        this.logger.debug("用户"+player.playerId+"下线b并广播");


        //通知其他玩家下线了
        player.area.broadcast(msgOutGame);
        session.closeNow();


    }

    /**
     * 在线则踢下线
     * @param playerId
     */
    private void kick(long playerId){
        boolean isOnline = PlayerManager.isOnline(playerId);
        if(isOnline){
            MsgKick msgKick = new MsgKick();
            Player playerOnline = PlayerManager.getPlayer(playerId);
            playerOnline.send(msgKick);
            playerOnline.update();
            playerOnline.close();
        }
    }

    /**
     * 进入游戏
     * @param session
     * @param msgEnterGame
     */
    private void enter(IoSession session, MsgEnterGame msgEnterGame) {

        long playerId = msgEnterGame.getPlayerId();
        //是否已经在游戏中，踢下线协议
        kick(playerId);

        PlayerEntity p = playerDao.findByPlayerId(playerId);
        if(p == null){
            msgEnterGame.setResult(1);
            NetManager.send(session, msgEnterGame);
            return;
        }

        //创建新用户信息
        Player player = new Player(session, playerDao, itemsManager);

        player.scene = p.getScene();
        player.areaId = p.getAreaId();
        player.playerEntity = p;
        player.playerId = p.getPlayerId();
        PlayerManager.addPlayer(player.playerId, player);

        //获取区域信息并添加入area
        Area area = NetManager.scenes.get(p.getScene())[p.getAreaId()];
//        for(long playerId2 :area.playerIds.keySet()){
//            System.out.println("before"+playerId2);
//        }
        area.addPlayer(p.getPlayerId());
        List<PlayerEntity> entityList = new ArrayList<PlayerEntity>();
        for(long playerId2 :area.playerIds.keySet()){
//            System.out.println("after"+playerId2);
            Player player1 = PlayerManager.getPlayer(playerId2);
            entityList.add(player1.playerEntity);
        }

        //组装协议
        PlayerEntity[] playerEntities2 = new PlayerEntity[entityList.size()];
        msgEnterGame.setPlayerEntities(entityList.toArray(playerEntities2));
        msgEnterGame.setPlayerId(p.getPlayerId());
        msgEnterGame.setPlayerName(p.getPlayerName());
        session.setAttribute("player", player);

        //装备
        List<ItemEntity> list = itemsManager.findItemsByPlayerId(p.getPlayerId());
        player.itemEntityList = list;
        ItemEntity[] items = new ItemEntity[list.size()];
        list.toArray(items);
        msgEnterGame.setItems(items);

        NetManager.send(session, msgEnterGame);
    }

    private String getAttributeJsonStr(JSONObject jsonObject){
        JSONObject attributeJson = new JSONObject();
        String type = jsonObject.getString("type");
        if(type.equals("Equipment")){
            int strength = jsonObject.getInt("strength");
            int itellect = jsonObject.getInt("intellect");
            int agility = jsonObject.getInt("agility");
            int stamina = jsonObject.getInt("stamina");
            attributeJson.put("strength",strength);
            attributeJson.put("intellect",itellect);
            attributeJson.put("agility",agility);
            attributeJson.put("stamina",stamina);
        }else if(type.equals("Weapon")){
            int damage = jsonObject.getInt("damage");
            attributeJson.put("damage",damage);
        }else if(type.equals("Material")){

        }else if(type.equals("Consumable")){
            int hp = jsonObject.getInt("hp");
            int mp = jsonObject.getInt("mp");
            attributeJson.put("hp",hp);
            attributeJson.put("mp",mp);
        }

        return attributeJson.toString();
    }

}
