package thread.classWork.semaphor.practic;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestServer {
    public static void main(String[] args) {
        RequestServer server = new RequestServer();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i <= 50 ; i++) {
            int current = i;
            Thread t = new Thread(()->{
                if (server.processRequest(current)){
                    System.out.println("Permit: "+server.semaphore.availablePermits());
                }

            });
        threads.add(t);
        t.start();
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });



    }
    private final Semaphore semaphore = new Semaphore(4);
    @Getter
    private final AtomicInteger processed = new AtomicInteger();
    @Getter
    private final AtomicInteger rejected = new AtomicInteger();

    public boolean processRequest(int requested) {
        boolean isTry = semaphore.tryAcquire();
        try {
            if (isTry) {

                System.out.printf("Processed: %s\n",requested);
                Thread.sleep(10000);
                processed.incrementAndGet();
                return true;
            }
            System.out.printf("Request %s rejected: server busy\n",requested);
            rejected.incrementAndGet();

            return false;
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }finally {

            if (isTry) {
               int total = processed.get()+rejected.get();

                semaphore.release();
            }
        }
    }
}
