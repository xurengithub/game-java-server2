package game.playerino.player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import game.playerino.Player;
public class PlayerManager {
    public static Map<Long,Player> players = new ConcurrentHashMap<Long, Player>();

    public static boolean isOnline(long playerId){
        return players.containsKey(playerId);
    }

    public static Player getPlayer(long playerId){
        if(players.containsKey(playerId)){
            return players.get(playerId);
        }

        return null;
    }

    public static void addPlayer(long playerId, Player player){
        players.put(playerId,player);
    }

    public static void removePlayer(long playerId){
        players.remove(playerId);
    }
}
