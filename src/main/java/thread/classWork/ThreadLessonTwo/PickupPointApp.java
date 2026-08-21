package thread.classWork.ThreadLessonTwo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PickupPointApp {
    public static void main(String[] args) throws InterruptedException {
        List<PickupWindow> windows = new ArrayList<>();
        windows.add(new PickupWindow(1));
        windows.add(new PickupWindow(2));
        List<Order>orders = new ArrayList<>();
        PickupPoint point = new PickupPoint(windows,orders);
        ReadyOrderQueue queue = new ReadyOrderQueue();

        List<Thread>threads = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(()->{
                synchronized (point.getOrders()) {
                    for (int j = 0; j < 100; j++) {
                        long id = PickupPoint.generateOrderId();
                        Order order = new Order("Customer- " + id, id);
                        point.getOrders().add(order);
                    }
                    System.out.println("Size-" + point.getOrders().size());
                }
            });
            thread.start();
            threads.add(thread);
        }

        for (Thread t : threads){
            t.join();
        }


        List<Thread>readyOrderTransferTreads = new ArrayList<>();

        for (int i = 0; i < 2; i++){
            Thread thread = new Thread(()->{
                synchronized (point.getOrders()) {
                    for (Order o : point.getOrders()) {
                        queue.addOrder(o);
                    }
                }
            });
            thread.start();
            readyOrderTransferTreads.add(thread);
    }
        readyOrderTransferTreads.forEach(r->{
            try {
                r.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        List<Thread>employeePickupPoint = new ArrayList<>();
        for (int i = 0; i < 8 ; i++) {
            Thread thread = new Thread(()->{
                try{
                    while (!Thread.currentThread().isInterrupted() && PickupPoint.getIssuedOrders().get() < 200 ){
                        Order order = queue.takeOrder();
                        if (order == null){
                            return;
                        }
                        boolean issued = false;
                        while (!issued && !Thread.currentThread().isInterrupted()){
                        for (PickupWindow window: point.getWindows()) {
                            if (window.tryServe(order, 50, TimeUnit.MILLISECONDS)) {
                                issued = true;
                                break;
                            }
                        }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
            employeePickupPoint.add(thread);
            thread.start();
        }

        employeePickupPoint.forEach(e-> {
            try {
                e.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });


        PickupMonitor monitor = new PickupMonitor(windows,queue);
        Thread monitorThread = new Thread(monitor);
        monitorThread.setDaemon(true);
        monitorThread.start();
        monitorThread.join();

        employeePickupPoint.forEach(e->{
            try {
                e.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

    }
}