package commons.events;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseUserEvent {
    public long uid;
    public int eventSource;
    public Timestamp timestamp;

    public BaseUserEvent(int eventSource, long uid){
        this.eventSource = eventSource;
        this.uid = uid;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = format.format(new Date());
        this.timestamp = Timestamp.valueOf(timeStr);
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getEventSource() {
        return eventSource;
    }

    public void setEventSource(int eventSource) {
        this.eventSource = eventSource;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
