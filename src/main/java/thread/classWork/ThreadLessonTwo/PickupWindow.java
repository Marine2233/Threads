package thread.classWork.ThreadLessonTwo;

import lombok.Getter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PickupWindow {
    @Getter
    private int id;
    private Lock lock = new ReentrantLock();
    @Getter
    private volatile AtomicInteger servedCustomers = new AtomicInteger();

    public PickupWindow(int id){
        this.id = id;
    }

    public int getWaitingClients(){
        return ((ReentrantLock)lock).getQueueLength();
    }

    public boolean isOverload(){
        return getWaitingClients() > 4;
    }

    public boolean tryServe(Order order, long timeout, TimeUnit unit){
        if (order == null) {
            return false;
        }

        boolean locked =false;
        try {
            locked = lock.tryLock(timeout, unit);
            if (locked) {

                if (order.startIssuing()){

                    order.completeIssued();
                    servedCustomers.incrementAndGet();
                    PickupPoint.incrementIssuedOrders();
                    System.out.printf("Заказ %s выдан\n",order.getId());
                    return true;
                }

            }
            return false;
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            return false;

        }finally {
            if (locked){
                lock.unlock();
            }
    }
}

}
