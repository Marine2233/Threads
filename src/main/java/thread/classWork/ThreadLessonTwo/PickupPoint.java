package thread.classWork.ThreadLessonTwo;
import lombok.Getter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
@Getter
public class PickupPoint {

    private final List<Order> orders;
    private final List<PickupWindow> windows;
    private static final AtomicLong id = new AtomicLong();
    @Getter
    private static final AtomicInteger issuedOrders = new AtomicInteger();

    public PickupPoint(List<PickupWindow>windows,List<Order>orders){
        this.windows = windows;
        this.orders = orders;
    }

    public static long generateOrderId() {
        return id.incrementAndGet();
    }

    public static void incrementIssuedOrders(){
        issuedOrders.incrementAndGet();
    }


}
