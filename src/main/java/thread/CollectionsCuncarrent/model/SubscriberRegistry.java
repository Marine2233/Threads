package thread.CollectionsCuncarrent.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SubscriberRegistry {
    private final CopyOnWriteArrayList<String> subscribers = new CopyOnWriteArrayList<>();
    public SubscriberRegistry() {
        subscribers.add("email-service");
        subscribers.add("audit-service");
        subscribers.add("metric-service");
        subscribers.add("admin-panel");
    }

    public void subscribe(String subscriber){
        subscribers.addIfAbsent(subscriber);
    }

    public void unsubscribe(String subscriber){
        subscribers.remove(subscriber);
    }

    public void notifySubscribers(SupportEvent event){
        subscribers.forEach(subscribe-> System.out.println(event));
    }

    public List<String> getSubscribers(){
        return List.copyOf(subscribers);
    }

}
