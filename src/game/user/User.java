package game.user;

import game.playerino.player.Player;
import org.apache.mina.core.session.IoSession;

import java.util.LinkedList;
import java.util.List;

public class User {

    private UserManager userManager;
    public Player player = new Player();
    public long uid;
    public List<Long> playerIds = new LinkedList<Long>();
    public IoSession session;
    private boolean isUsed = false;

    public void init(IoSession session){
        this.session = session;
    }

    public boolean isUsed(){
        return isUsed;
    }

    public void login(String userName, String password){
        User user = (User) session.getAttribute("user");
        if(user == null){
            long uid = userManager.login(userName,password);
            if(uid != -1){
                this.uid = uid;
                session.setAttribute("user", this);
                session.write("ok");
                return;
            }
        }
        session.write("fail");
    }

    public void addPlayerId(long playerId){
        if(!playerIds.contains(playerId)){
            playerIds.add(playerId);
        }
    }
    public void addPlayerIds(List<Long> ids){
        ids.removeAll(playerIds);
        playerIds.addAll(ids);
    }
    private void getPlayers(){

    }

    public void register(String userName, String password){
        if(userManager.register(userName, password)){
            session.write("ok");
            return;
        }

        session.write("fail");

    }

    public void unUse(){
        isUsed = false;
        uid = 0;
        if(player != null){
            if(player.playerId != 0){
//                player.outPlayer();
            }
        }
    }
}
