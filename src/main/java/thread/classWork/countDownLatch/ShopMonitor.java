package thread.classWork.countDownLatch;

public class ShopMonitor implements Runnable{
    private final ShopServer server ;
    private volatile boolean running = true;

    public ShopMonitor(ShopServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (running)
        try {
            Thread.sleep(500);
            System.out.printf("Свободных request slots: %s\n",server.getRequestSlots().getQueueLength());

            System.out.printf("Обработано клиентов: %s\n",server.getCountServicedClients());

            System.out.printf("Отклонено: %s\n",server.getDontService());

            System.out.printf("Выручка: %s\n" ,server.getTotalRevenue());

            running= false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
