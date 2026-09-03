package thread.classWork.practicworc;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class ServerApp {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(8);
        CountDownLatch latch = new CountDownLatch(1);
        Statistic statistic = new Statistic(semaphore);
        Database database = new Database(semaphore);
        Cache cache = new Cache(semaphore);
        FilesStorage storage = new FilesStorage(semaphore);
        Random random = new Random();
        Monitor monitorRunnable = new Monitor();
        Thread monitor = new Thread(monitorRunnable);
        monitor.setDaemon(true);
        monitor.start();

        for (int i = 0; i <= 50; i++){
            Thread thread = new Thread(()->{
                int next = random.nextInt(1,6);
                switch (next){
                    case 1:statistic.createReport();
                    case 2:database.backup();
                    case 3:cache.clearCache();
                    case 4:storage.saveUser();
                    case 6:storage.loadUsers();
                }
            });
            thread.start();
            latch.countDown();
        }
    }
}
