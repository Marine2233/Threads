package thread.classWork.countGownLatch;

import lombok.Getter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShopServer {

    @Getter
    private final Semaphore requestSlots = new Semaphore(5);
    private final Lock lock = new ReentrantLock();
    @Getter
    private volatile long totalRevenue ;
    @Getter
    private volatile int  dontService ;
    @Getter
    private volatile int countServicedClients;
    private Object monitor = new Object();

    public ShopServer() {
        totalRevenue = 0;
        dontService = 0;
        countServicedClients = 0;
    }


    public boolean processRequest(int clientId,int price, long timeout, TimeUnit unit){

        try {

            boolean ok = requestSlots.tryAcquire(timeout,unit);

            if (!ok) {
                System.out.println("Запрос отклонен. Client- "+clientId);
                synchronized (monitor) {
                    dontService+=1;
                }
                return false;
            }
                lock.lock();
            System.out.println("Запрос выполнен. Client- "+clientId);
                totalRevenue += price;
                countServicedClients+=1;
                return true;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
            requestSlots.release();
        }

    }
}
