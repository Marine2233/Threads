package thread.CollectionsCuncarrent.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/**Подписчики добавляются/удаляются редко, но список читается при огромном
 количестве событий.
 Именно поэтому здесь подходит CopyOnWriteArrayList .
 Эксперимент
 Создать отдельный поток, который во время работы программы иногда добавляет
 удаляет подписчика.
 Одновременно consumers постоянно вызывают:
 Java
 notifySubscribers(...
 После выполнения объяснить, почему CopyOnWriteArrayLife подходит сюда, но не
 подошёл бы для хранения всех Ticket, )ых создаются тысячи.
 */
public class SubscriberRegistry {
    private final CopyOnWriteArrayList<String> subscribers;

    public SubscriberRegistry() {
        this.subscribers = new CopyOnWriteArrayList<>();
        this.subscribers.add("email-service");
        this.subscribers.add("audit-service");
        this.subscribers.add("metrics-service");
        this.subscribers.add("admin-panel");
    }

    public void subscribe(String subscriber){
        subscribers.add(subscriber);
    }
    public void unsubscribe(String subscriber){
        subscribers.remove(subscriber);
    }
    public void notifySubscribers(SupportEvent event){
        if (event == null) return;
        subscribers.forEach(subscriber-> System.out.printf("[Notification] Отправлено в %s -> Событие ID: %s;\n",
                subscriber, event.getTicketId()));

    }
    public List<String> getSubscribers(){
        return List.copyOf(subscribers);
    }

}
