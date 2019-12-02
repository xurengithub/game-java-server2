package core.netserver.protocol;

public class ProtocolStr extends ProtocolBase {
    //传输的字符串
    public String str;

    //解码器
    public ProtocolBase decode(byte[] readbuff, int start, int length)
    {
        ProtocolStr protocol = new ProtocolStr();
        protocol.str = new String(readbuff, start, length);
        return protocol;
    }

    //编码器
    public byte[] encode()
    {
        byte[] b = str.getBytes();
        return b;
    }

    //协议名称
    public String getName()
    {
        if (str.length() == 0) return "";
        return str.split(":")[0];
    }

    //协议描述
    public String getDesc()
    {
        return str;
    }
}
