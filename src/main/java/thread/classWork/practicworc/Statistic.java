package thread.classWork.practicworc;

import lombok.Getter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Getter
public class Statistic {
    private Semaphore semaphore;
    private Lock lock = new ReentrantLock();

    public Statistic(Semaphore semaphore) {
        this.semaphore = semaphore;
    }


    public void createReport() {
        try {
            semaphore.acquire();
            lock.lock();
            boolean isLock = true;
            try {
                System.out.println("Statistic work");
                Thread.sleep(1000);
                System.out.println("Create report work");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);

            } finally {
                if (isLock) {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            semaphore.release();
        }
    }
}
