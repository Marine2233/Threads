package thread.classWork.countDownLatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PointApplication {
    public static void main(String[] args) {
        CountDownLatch startupLatch = new CountDownLatch(4);

        for (int i =0; i < 4; i++){
            Thread thread = new Thread(new ServiceLoader("Service loader "+i,startupLatch));
            thread.start();
        }

        try {
            startupLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Thread>clientsThread = new ArrayList<>();
        ShopServer server = new ShopServer();
        Random random = new Random();
        for (int i =0; i < 100; i++){
            final int id = i;
            Thread client = new Thread(()->{
                int price = random.nextInt(1200,22000);
                server.processRequest(id,price,1500, TimeUnit.MILLISECONDS);
            });
            clientsThread.add(client);
            client.start();

        }

        clientsThread.forEach(client->{
            try {
                client.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread monitor = new Thread(new ShopMonitor(server));
        monitor.setDaemon(true);
        monitor.start();
        try {
            monitor.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Maim finished.");

    }
}
