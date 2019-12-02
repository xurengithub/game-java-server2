package core.netserver;

import core.utils.ArrayUtils;
import core.utils.ServNetUtils;
import game.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class Server {

    public int maxConn = 50;
    public static ArrayList<Conn> conns;
    public ExecutorService pool;

    //定时器
    public Timer timer = new Timer();
    //心跳时间
    public long heartBeatTime = 60000;

    @Autowired
    private UserManager userManager;

    /**
     * 构造服务，初始化Conn socket连接池 和线程池
     */
    public Server(){
        conns = new ArrayList<Conn>(maxConn);
        pool = Executors.newCachedThreadPool();
    }

    /**
     * 下一个可用Conn的索引
     * @return
     */
    private int newIndex() {
        if (conns == null){
            return -1;
        }
        for (int i = 0; i < conns.size(); i++)
        {
            if (conns.get(i) == null)
            {
                conns.set(i, new Conn());
                return i;
            }
            else if (conns.get(i).isUse == false)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * 开启服务
     */
    public void start(){

        //开启定时任务
        handlerTimer();

        for (int i = 0;i<maxConn;i++){
            conns.add(new Conn());
        }
        try {
            ServerSocket serverSocket = new ServerSocket(8090);
            while (true){
                Socket socket = serverSocket.accept();
                int index = newIndex();
                if(index < 0){
                    socket.close();
                    System.out.println("警告:连接已满");
                }else {
                    Conn conn = conns.get(index);
                    conn.init(socket);
                    System.out.println(index+"建立连接");


                    pool.submit(new ServerRun(conn));
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理定时任务(检查心跳时间，过时conn.close)
     */
    private void handlerTimer(){
        //定时器检查心跳
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Conn conn: conns){
                    if(conn != null && conn.isUse){
                        int last = (int)conn.lastTickTime;
                        int now = (int)System.currentTimeMillis();
                        if((last+heartBeatTime)<now){
                            conn.close();
                        }
                    }
                }
            }
        },heartBeatTime,heartBeatTime);
    }

    /**
     * 关闭服务
     */
    public void close(){
        timer.cancel();
        pool.shutdown();
        for (Conn conn:conns){
            conn.close();
        }
    }

    /**
     * 内部线程Runable类
     */
    private class ServerRun implements Runnable {

        private Conn conn;
        private OutputStream out;
        private InputStream in;



        public ServerRun(Conn conn){
            this.conn = conn;
        }

        @Override
        public void run() {
            try {
                in = conn.socket.getInputStream();
                while((conn.buffCount = in.read(conn.readBuff, conn.buffCount, conn.readBuff.length)) != -1){
//                    System.out.println(new String(conn.readBuff));
                    processData(conn);
                }
            } catch (IOException e) {
//                e.printStackTrace();
                conn.close();
            }
        }

        /**
         * 处理数据粘包数据
         * @param conn
         */
        public void processData( Conn conn){
            conn.lastTickTime = System.currentTimeMillis();

            ServNetUtils.copy(conn.readBuff, conn.lenBytes, Conn.INT32);

//            System.out.println(ServNetUtils.byteArrayToInt(conn.lenBytes));
//            System.out.println(new String(conn.readBuff));
//            for(byte b:conn.readBuff){
//                System.out.print(b+" ");
//            }
//            System.out.println("\n");

            conn.msgLength = ServNetUtils.byteArrayToInt(conn.lenBytes);

            if(conn.msgLength <= conn.buffCount-Conn.INT32){

                String msg = new String(conn.readBuff, Conn.INT32, conn.msgLength);
                dispatch(msg);
                ServNetUtils.copy(conn.readBuff, Conn.INT32 + conn.msgLength, conn.readBuff, 0, conn.buffCount - (Conn.INT32 + conn.msgLength));
                conn.buffCount = conn.buffCount - (Conn.INT32 + conn.msgLength);


                if(conn.buffCount > 0){
                    processData(conn);
                }
            }

        }

        /**
         * 分发msg，处理msg
         * @param msg
         */
        public void dispatch(String msg){
            String[] arr = msg.split("%");
            switch (arr[0]){
                case "login":response(userManager.login(arr[1],arr[2]) !=-1 ? "ok":"fail");
                break;
                case "register":response(userManager.register(arr[1],arr[2]) == true ? "ok":"fail");
                break;
                case "ACTION":
                case "TRANS":
                case "LEAVE":
                case "POS":distributeMsg(msg);
                break;
                default:response("未知协议");
                break;

            }
        }

        /**
         * 返回一个msg
         * @param responseMsg
         */
        public void response(String responseMsg){
            try {
                out = conn.socket.getOutputStream();
                out.write(responseMsg.getBytes());
                out.flush();
            } catch (IOException e) {
                conn.close();
            }
        }

        /**
         * 将msg消息通知除了本连接其所有连接
         * @param msg
         */
        public void distributeMsg(String msg){
            byte[] msgBodyBytes = msg.getBytes();
            byte[] msgLenBytes = ServNetUtils.intToByteArray(msgBodyBytes.length);
            byte[] totalMsgBytes = ArrayUtils.Connect(msgLenBytes,msgBodyBytes);
//            System.out.println(msg);
            OutputStream out;
            for (Conn conn:Server.conns){
//                if(this.conn != conn){
                    if(conn.isUse){
                        try {
                            out = conn.socket.getOutputStream();
                            out.write(totalMsgBytes);
                            out.flush();
                        } catch (IOException e) {
                            conn.close();
                        }
                    }
//                }
            }
        }


    }
}
