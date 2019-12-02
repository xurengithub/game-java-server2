package core.netserver;

import java.io.IOException;
import java.net.Socket;

public class Conn {
    public static final int BUFFER_SIZE = 1024;
    public static final int INT32 = 4;
    public Socket socket;
    public boolean isUse = false;

    public byte[] readBuff = new byte[BUFFER_SIZE];

    public int buffCount = 0;

    public byte[] lenBytes = new byte[INT32];

    public int msgLength = 0;

    public long lastTickTime = 0;

//    public Player player;

    public Conn(){
    }

    public void init(Socket socket){
        this.socket = socket;
        isUse = true;
        buffCount = 0;
        lastTickTime = System.currentTimeMillis();

    }

    public int buffRemain(){
        return BUFFER_SIZE - buffCount;
    }

    public String getAdress(){
        if(!isUse){
            return "无法获取地址";
        }
        return socket.getInetAddress().getHostName();
    }

    public void close(){
        if(!isUse){
            return ;
        }
//        if(player != null){
//            return ;
//        }

        System.out.println("断开连接!");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isUse = false;
    }


}
