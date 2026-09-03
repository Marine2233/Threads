package thread.classWork.practicworc;

import lombok.Getter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@Getter
public class FilesStorage {

    private Semaphore semaphore ;
    private Lock lock = new ReentrantLock();

    public FilesStorage(Semaphore semaphore) {
        this.semaphore = semaphore;
    }


    public boolean saveUser() {
        try {
            semaphore.acquire();
            lock.lock();
            boolean isLock = true;
            try {
                System.out.println("FileStorage work");
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (isLock) {
                    lock.unlock();
                    return true;
                }
                return false;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            semaphore.release();
        }
    }

    public void loadUsers(){

        lock.lock();
        boolean isLock = true;
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if (isLock){
                lock.unlock();
            }
        }

    }
}
