package thread.classWork.HWforThreadAndJackson;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ShopServer {
    private final Semaphore requestSlots = new Semaphore(4);

    public boolean processRequest(int clientId, long timeout, TimeUnit unit){
        boolean connected = false;
        try {
            connected = requestSlots.tryAcquire(timeout, unit);
            if (connected) {
                System.out.println("request done");
                return true;
            }
            System.out.println("request rejected.");
            return  false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if (connected){
                requestSlots.release();
            }
        }
    }

}
