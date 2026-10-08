package thread.parcelCentre.model;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
@Getter
public class ParcelStatistics {
    private final AtomicInteger created = new AtomicInteger();
    private final AtomicInteger processing = new AtomicInteger();
    private final AtomicInteger sorted = new AtomicInteger();

    public void incCreated(){
        created.incrementAndGet();
    }

    public void incProcessing(){
        processing.incrementAndGet();
    }
    public void decProcessing(){
        processing.decrementAndGet();
    }

    public void incSorted(){
        sorted.incrementAndGet();
    }
}
