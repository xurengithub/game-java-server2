package client2;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    public Socket socket;

    public ClientThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            InputStream in = socket.getInputStream();
            while (true){
                if((len = in.read(buffer)) != -1){
                    String str = new String(buffer,0,len);
                    System.out.println(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
