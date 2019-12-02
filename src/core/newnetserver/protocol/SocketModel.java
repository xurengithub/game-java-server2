package core.newnetserver.protocol;

import core.utils.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

public class SocketModel {
    public byte type;
    public byte command;
    public String message;

    public SocketModel(){

    }
    public SocketModel(byte[] body){
        this.type = body[0];
//        this.area = ServNetUtils.byteArrayToInt(Arrays.copyOfRange(body, 1, 5));
//        this.command = body[5];
//        this.message = new String(Arrays.copyOfRange(body, 6, body.length));//反序列化
        this.command = body[1];
        this.message = new String(Arrays.copyOfRange(body, 2, body.length-1));//反序列化
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }


    public byte getCommand() {
        return command;
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    @Override
    public String toString() {
        return type+" "+command+" "+message;
    }

    public byte[] getBytes(){
        byte[] msg = message.getBytes();
        byte[] bytes = new byte[2+msg.length];
        bytes[0] = type;
        bytes[1] = command;
        System.arraycopy(msg, 0,bytes, 2, msg.length);
        return bytes;
    }
}
