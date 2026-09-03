package thread.classWork.practicworc;

import lombok.Getter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Getter
public class Cache {
    private Semaphore semaphore;
    private Lock lock = new ReentrantLock();

    public Cache(Semaphore semaphore ) {
        this.semaphore = semaphore;
    }


    public void clearCache(){
        boolean isLock =false;

        try {
            semaphore.acquire();

            lock.lock();
            isLock = true;
        try{
            System.out.println("Cache work");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (isLock) {
                lock.unlock();
            }

        }
    }
}
