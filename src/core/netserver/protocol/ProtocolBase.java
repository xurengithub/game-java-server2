package core.netserver.protocol;

public class ProtocolBase {
    // 解码器，解码readbuff中从start开始的length字节
    public ProtocolBase decode(byte[] readbuff, int start, int length)
    {
        return new ProtocolBase();
    }

    // 编码器
    public byte[] encode()
    {
        return new byte[] { };
    }

    // 协议名称，用于消息分发
    public String getName()
    {
        return "";
    }

    //描述
    public String getDesc()
    {
        return "";
    }
}
