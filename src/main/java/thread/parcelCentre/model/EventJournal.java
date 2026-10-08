package thread.parcelCentre.model;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class EventJournal {
    private final ConcurrentLinkedQueue<String>events = new ConcurrentLinkedQueue<>();
    private AtomicInteger size = new AtomicInteger();

    public EventJournal() {

    }

    public void addEvent(String event){
        if (event == null)return;
        events.offer(event);
        size.incrementAndGet();
    }

    public String getNextEvent(){
        String event = events.poll();
        if (event != null) {
            size.decrementAndGet();
        }
        return event;
    }

    public String peekEvent(){
        return events.peek();
    }

    public int size(){
        return size.get();
    }

}
