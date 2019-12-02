package core.newnetserver;

import configuration.AllConfiguration;
import game.message.MessageManager;

import game.playerino.Player;
import game.playerino.player.PlayerManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Timer;
import java.util.TimerTask;


public class ServerEnter {
    private static int saveInterval = 60000;
    private static int startSaveTimerTime = 60000;
    public static void main(String[] args) {
        //初始化容器
        ApplicationContext context = new AnnotationConfigApplicationContext(AllConfiguration.class);
        //初始化消息处理器映射
        MessageManager.init(context);
        //启动mina
        NetManager.startMina(8090);

        SaveTimer();
        //定时保存player数据
    }

    private static void SaveTimer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("players数量:"+PlayerManager.players.size());
                for(Player player : PlayerManager.players.values()){
                    player.update();
                }
            }
        },startSaveTimerTime,saveInterval);
    }
}
