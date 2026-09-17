package thread.ThreadPool;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Statistic {
    private AtomicInteger attempts = new AtomicInteger();
    private AtomicInteger successfulBookings = new AtomicInteger();

    public void incAttempts(){
        attempts.incrementAndGet();
    }

    public void  incSucc(){
        successfulBookings.incrementAndGet();
    }

}
