package thread.classWork.ThreadLessonTwo;

import java.util.List;

public class PickupMonitor implements Runnable{
    private List<PickupWindow>windows;
    private ReadyOrderQueue queue;
    private volatile boolean running = true;

    public PickupMonitor(List<PickupWindow>windows,ReadyOrderQueue queue){
        this.windows = windows;
        this.queue=queue;
    }

    public boolean stop(){
        return running=false;
    }
    @Override
    public void run() {
        System.out.println("=".repeat(10) + " Load monitor " + "=".repeat(10));
        while (!Thread.currentThread().isInterrupted() && running) {
            windows.forEach(w -> {
                if (!Thread.currentThread().isInterrupted()&&running) {
                    System.out.println("Window- " + w.getId());
                    System.out.println("Queue= " + w.getWaitingClients());
                    String overload = w.isOverload() ? "Overload" : "OK";
                    System.out.println(overload);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            System.out.println("Ready orders= " + queue.sizeReadyOrders());
            System.out.println("Issued= " + PickupPoint.getIssuedOrders());
            stop();
        }
    }
}
