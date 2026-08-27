package thread.classWork.HWforThreadAndJackson;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceLoader implements Runnable{

    private final String serviceName;
    private final CountDownLatch startupLatch;
    private final AtomicInteger successfullyLoaded;
    private final AtomicInteger failedLoads ;
    private final Semaphore semaphore = new Semaphore(4);

    public ServiceLoader(String serviceName, CountDownLatch startupLatch,AtomicInteger failedLoads ,AtomicInteger successfullyLoaded) {
        this.serviceName = serviceName;
        this.startupLatch = startupLatch;
        this.failedLoads  = failedLoads;
        this.successfullyLoaded = successfullyLoaded;
    }

    @Override
    public void run() {
        System.out.println("Service load started.");
        try{
            Thread.sleep(1000);
            successfullyLoaded.incrementAndGet();
            System.out.println(serviceName);

        } catch (InterruptedException e) {
            System.out.println("Load service interrupted.");
            failedLoads.incrementAndGet();
            Thread.currentThread().interrupt();
            throw new RuntimeException(e)
                    ;
        }finally {
            startupLatch.countDown();

        }
    }
}
