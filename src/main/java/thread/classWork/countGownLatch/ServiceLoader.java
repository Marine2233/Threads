package thread.classWork.countGownLatch;


import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ToString
public class ServiceLoader implements Runnable{

    private final String serviceName ;
    private final CountDownLatch startupLatch;
    private final AtomicInteger successfullyLoaded = new AtomicInteger();
    private final AtomicInteger failedLoads = new AtomicInteger();


    public ServiceLoader(String serviceName, CountDownLatch startupLatch) {
        this.serviceName = serviceName;
        this.startupLatch = startupLatch;
    }

    @Override
    public void run() {

        try {
            System.out.println("Загрузка: " + serviceName);
            Thread.sleep(1000);
            successfullyLoaded.incrementAndGet();
        } catch (InterruptedException e) {
            failedLoads.incrementAndGet();
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }finally {
            System.out.printf("\nУспешных загрузок- %s для- %s \n",successfullyLoaded,serviceName);
            System.out.printf("Прерванные загрузки-%s у %s ",failedLoads,serviceName);
            startupLatch.countDown();
        }

    }
}
