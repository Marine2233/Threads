package thread.classWork.HWforThreadAndJackson;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
@Getter
public class OrderStatistics {
    private final AtomicInteger processed0rders;
    private final AtomicInteger failedOrders;
    private final AtomicInteger rejected0rders;
    private final AtomicLong totalRevenue;

    public OrderStatistics(){
        processed0rders = new AtomicInteger();
        failedOrders = new AtomicInteger();
        rejected0rders = new AtomicInteger();
        totalRevenue = new AtomicLong();
    }

    public void incrementProcessed(){
        processed0rders.incrementAndGet();
    }

    public void  incrementFailed(){
        failedOrders.incrementAndGet();
    }

    public void incrementRejected(){
        rejected0rders.incrementAndGet();
    }

    public void addRevenue(long amount){
        totalRevenue.addAndGet(amount);
    }


}
