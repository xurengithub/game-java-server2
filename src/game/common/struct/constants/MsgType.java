package game.common.struct.constants;

public class MsgType {

    /**
     * 用户操作
     */
    public static final byte USER_REGISTER = 1;
    public static final byte USER_LOGIN = 2;
    public static final byte USER_LOGOUT = 3;
    public static final byte USER_CREATE_PLAYER = 4;

    /**
     * 角色操作
     */
    public static final byte PLAYER_ENTER = 1;
    public static final byte PLAYER_OUT = 2;
    public static final byte PLAYER_POSITION = 3;
    public static final byte PLAYER_ATTACK = 4;
    public static final byte PLAYER_BUY_PROP = 5;

}
