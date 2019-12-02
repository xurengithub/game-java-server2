package core.newnetserver;

import game.metadata.SceneData;
import game.playerino.Player;
import game.scene.Area;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import proto.MsgBase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

public class NetManager {
    public static Set<IoSession> sessions = Collections.synchronizedSet(new HashSet<IoSession>());
//    public static Set<Player> players = Collections.synchronizedSet(new HashSet<Player>());
    public static Map<String, Area[]> scenes = Collections.synchronizedMap(new HashMap<String, Area[]>());
    public static void startMina(int post){
        try {
            NioSocketAcceptor acceptor = new NioSocketAcceptor();
            acceptor.setHandler(new MyServerHandler());
            acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 91);
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyTextLineFactroy()));
            acceptor.bind(new InetSocketAddress(post));

        } catch (IOException e) {
            e.printStackTrace();
        }
        initScene();
    }

    private static void initScene(){
        JSONObject[] jsonObjects = SceneData.jsonObjects;
//        System.out.println(jsonObjects[0].toString());
        for (int i = 0; i < jsonObjects.length; i++) {
            JSONArray jsonAreas = jsonObjects[i].getJSONArray("areas");
            JSONObject[] jobjs = new JSONObject[jsonAreas.size()];
            jsonAreas.toArray(jobjs);
            Area[] areas = new Area[jsonAreas.size()];
            for(int j = 0;j<jobjs.length;j++){
                areas[j] = (Area) JSONObject.toBean(jobjs[j], Area.class);
            }
            scenes.put(jsonObjects[i].getString("sceneName"), areas);

//            for (int x=0;x<areas.length;x++){
//                System.out.println(areas[x].getX());
//            }
        }
        //读取区域配置
        //初始化scenes
    }
    public static void send(IoSession session, MsgBase msgBase){
        session.write(msgBase);
    }
}
