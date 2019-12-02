package game.message;

import core.newnetserver.protocol.SocketModel;
import game.common.Tools;
import game.message.handler.AbstractHandler;
import game.user.UserPool;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import proto.MsgBase;

public class MessageManager {
    private static Logger logger = LoggerFactory.getLogger(MessageManager.class);
    /**消息类型配置文件*/
    private static Tools properties;

    public static UserPool userPool;

    private static ApplicationContext context1;


    public static void init(ApplicationContext context){
        properties = new Tools();
        properties.getProperties("messageType.properties");
        userPool = UserPool.getInstance();

        context1 = context;
        return;
    }

    /**
     * 接收消息，并将消息发送到相对应的消息处理者处理
     */
    public static void msgTransfer(IoSession session, MsgBase msgBase){
        AbstractHandler handler = (AbstractHandler) context1.getBean(properties.getStringFromProperty(msgBase.protoName+""));
        handler.handle(session, msgBase);
    }
}
