package thread.classWork.semaphor.practic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadService {
    public static void main(String[] args) {
        DownloadService service = new DownloadService();
        List<Thread> threads = new ArrayList<>();
        Thread t;

        for (int i = 0; i < 20 ; i++) {
             t = new Thread(()->{
                service.download("file.txt");
            });

            threads.add(t);
            t.start();
        }

        threads.forEach(t1-> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }
    private final Semaphore semaphore = new Semaphore(3);
    private final AtomicInteger activeDownloads = new AtomicInteger();
    private final AtomicInteger completedDownloads = new AtomicInteger();

    public void download(String fileName) {

            try {
                    semaphore.acquire();
                    activeDownloads.incrementAndGet();
                    System.out.println("Thread-4 начал загрузку: " + fileName +
                            ". Активных загрузок: \n" + activeDownloads.get());
                    Thread.sleep(2000);
                    activeDownloads.decrementAndGet();
                    completedDownloads.incrementAndGet();
                    System.out.println("Завершенных загрузок: " + completedDownloads.get());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
    }
}
