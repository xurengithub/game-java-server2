package commons.events;

public class LoginEvent extends BaseUserEvent {
    public String userName;
    public String password;
    public LoginEvent(int eventSource, long uid, String userName, String password) {
        super(eventSource, uid);
        this.password = password;
        this.userName = userName;
    }
}
