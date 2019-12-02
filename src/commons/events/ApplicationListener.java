package commons.events;

public interface ApplicationListener {
    public long onApplicationEvent(BaseUserEvent event);
}
