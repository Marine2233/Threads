package thread.classWork.practicworc;

import lombok.Getter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Getter
public class Database {

    private Semaphore semaphore;
    private Lock lock = new ReentrantLock();

    public Database(Semaphore semaphore) {
        this.semaphore = semaphore;
    }


    public void backup() {

        try {
            semaphore.acquire();
            lock.lock();
            boolean isLock = true;
            try {
                System.out.println("DB work");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
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
