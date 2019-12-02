package client2;

import core.utils.ArrayUtils;
import core.utils.ServNetUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public Socket socket;
    public byte[] buffer;
//    public String hostName;
//    public int port;


    public Client(String hostName,int port){
        socket = new Socket();

        try {
            socket.connect(new InetSocketAddress(hostName,port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     * @param msg
     */
    public void sendMsg(byte type1, byte command1, String msg){
        try {
            OutputStream out = socket.getOutputStream();
//            PrintStream ps = new PrintStream(out);
//            ps.println(msg);
            byte[] msgBytes = msg.getBytes();
//            System.out.println("msgBytesSize"+msgBytes.length);
//            byte[] msgLenBytes = ServNetUtils.intToByteArray(msgBytes.length);
//            System.out.println("msgLenBytes"+msgLenBytes.length);
            byte type =type1;
            byte command = command1;
            out.write(type);
            out.write(command);
//            out.write(ArrayUtils.Connect(msgLenBytes, msgBytes));
            out.write(msgBytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
//        Client client = new Client("154.8.172.144",8090);
        Client client = new Client("127.0.0.1",8090);
        new ClientThread(client.socket).start();

//        for(int i=0;i<20;i++){
////            client.sendMsg("position:xurenzuishuai");
//
//            client.sendMsg("login:xuren8023:xuren8023");
//        }
        Scanner scanner = new Scanner(System.in);
        while (true){
            String msg = scanner.nextLine();
            String arr[] = msg.split(" ");
            byte type = Byte.parseByte(arr[0]);
            byte command = Byte.parseByte(arr[1]);
            client.sendMsg(type,command,arr[2]+"\n");
            if(msg.equals("bye")){
                break;
            }

        }
    }


}
