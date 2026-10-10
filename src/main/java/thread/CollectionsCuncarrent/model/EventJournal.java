package thread.CollectionsCuncarrent.model;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class EventJournal {
    private final ConcurrentLinkedQueue<SupportEvent>events;
    private AtomicLong size = new AtomicLong();

    public EventJournal() {
        this.events = new ConcurrentLinkedQueue<>();
    }

    public long size(){
        return size.get();
    }

    public SupportEvent peek(){
        return events.peek();
    }

    public SupportEvent poll(){
        SupportEvent old = events.poll();
        if (old!=null) {
            size.decrementAndGet();
        }
       return old;
    }

    public void add(SupportEvent event){
        size.incrementAndGet();
        events.add(event);
    }
}
