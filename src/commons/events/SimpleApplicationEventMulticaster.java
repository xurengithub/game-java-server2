package commons.events;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SimpleApplicationEventMulticaster {
    public ArrayList<ApplicationListener> listeners = new ArrayList<ApplicationListener>(20);

    public void addListener(ApplicationListener listener){
        listeners.add(listener);
    }

    public void multicastEvent(BaseUserEvent event){
        for(ApplicationListener listener: listeners){
            listener.onApplicationEvent(event);
        }
    }
}
