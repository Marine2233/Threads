package thread.javaExecutorService;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
@Getter
public class ProcessingStatistic{
    public static final AtomicInteger COMPLETED = new AtomicInteger(0);
    public static final AtomicInteger REJECTED = new AtomicInteger(0);
    public static final AtomicInteger INTERRUPTED = new AtomicInteger(0);
    public static final AtomicInteger NOT_STARTED = new AtomicInteger(0);


    public static void incProcessNotStarted(){
        NOT_STARTED.incrementAndGet();
    }
    public static void incProcessIRejected(){
        REJECTED.incrementAndGet();
    }
    public static void incProcessCompleted(){
        COMPLETED.incrementAndGet();
    }
    public static void incProcessInterrupted(){
        INTERRUPTED.incrementAndGet();
    }
}
