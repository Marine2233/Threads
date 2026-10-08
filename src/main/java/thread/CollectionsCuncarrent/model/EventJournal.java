package thread.CollectionsCuncarrent.model;

import lombok.Getter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class EventJournal {
    private final ConcurrentLinkedQueue<SupportEvent> events;
    @Getter
    private final AtomicLong eventCount = new AtomicLong(0);

    public EventJournal() {
        this.events = new ConcurrentLinkedQueue<>();
    }

    public void add(SupportEvent event){
        if (event == null){
            return;
        }
        events.offer(event);
        eventCount.incrementAndGet();
    }

    public SupportEvent poll(){
        SupportEvent event = events.poll();
        if (event != null){
            eventCount.decrementAndGet();
        }
        return event;
    }
    public SupportEvent peek(){
        return events.peek();
    }
    public int size(){
        return (int) eventCount.get();
    }



}
