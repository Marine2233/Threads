package thread.CollectionsCuncarrent.model;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


@Getter
public class SupportStatistics {
    private final AtomicInteger created = new AtomicInteger();
    private final AtomicInteger processing= new AtomicInteger();
    private final AtomicInteger completed= new AtomicInteger();
    private final AtomicInteger failed= new AtomicInteger();
    private final AtomicInteger retries= new AtomicInteger();
    private final AtomicLong events= new AtomicLong();

    public void incEvent(){
        events.incrementAndGet();
    }

    public void decEvent(){
        events.decrementAndGet();
    }

    public void incRetries(){
        retries.incrementAndGet();
    }

    public void decRetries(){
        retries.decrementAndGet();
    }

    public void incFailed(){
        failed.incrementAndGet();
    }

    public void decFailed(){
        failed.decrementAndGet();
    }

    public void incCompleted(){
        completed.incrementAndGet();
    }

    public void decCompleted(){
        completed.decrementAndGet();
    }

    public void incProcessing(){
        processing.incrementAndGet();
    }

    public void decProcessing(){
        processing.decrementAndGet();
    }

    public void incCreated(){
        created.incrementAndGet();
    }

    public void decCreate(){
        created.decrementAndGet();
    }

}
