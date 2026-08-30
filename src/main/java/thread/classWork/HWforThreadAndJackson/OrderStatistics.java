package thread.classWork.HWforThreadAndJackson;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
@Getter
public class OrderStatistics {
    private final AtomicInteger processedOrders;
    private final AtomicInteger failedOrders;
    private final AtomicInteger rejectedOrders;
    private final AtomicLong totalRevenue;

    public OrderStatistics(){
        processedOrders = new AtomicInteger();
        failedOrders = new AtomicInteger();
        rejectedOrders = new AtomicInteger();
        totalRevenue = new AtomicLong();
    }

    public void incrementProcessed(){
        processedOrders.incrementAndGet();
    }

    public void  incrementFailed(){
        failedOrders.incrementAndGet();
    }

    public void incrementRejected(){
        rejectedOrders.incrementAndGet();
    }

    public void addRevenue(long amount){
        totalRevenue.addAndGet(amount);
    }


}
