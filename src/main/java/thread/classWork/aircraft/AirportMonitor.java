package thread.classWork.aircraft;

public class AirportMonitor implements Runnable{
    private volatile boolean running = true;
    private Airport airport;

    public AirportMonitor(Airport airport) {
        this.airport = airport;
    }


    @Override
    public void run() {
        while (running &&!Thread.currentThread().isInterrupted()) {
            if (running) {
                System.out.printf("Свободных полос: %s", airport.getRunaway().availablePermits());
                System.out.printf("\nПриземлилось: %s", airport.getLanded());
                System.out.printf("\nВзлетело: %s", airport.getDeparted());
                System.out.printf("\nОтклонено: ", airport.getRejected());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().isInterrupted();
                throw new RuntimeException(e);
            }
        }
        running = false;

    }
}
