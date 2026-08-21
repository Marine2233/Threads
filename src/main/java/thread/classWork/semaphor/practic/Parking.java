package thread.classWork.semaphor.practic;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@ToString
public class Parking {
    @SneakyThrows
    public static void main(String[] args) {
        Parking parking = new Parking();
        List<Thread>threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int idx = i;
            Thread thread = new Thread(()->{
                final int ind = idx;
                parking.enter("Car: " + ind);
            });
            threads.add(thread);
            thread.start();
        }


        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        System.out.println("Total cars: "+parking.totalCars);
        System.out.println("Occupied places: "+parking.occupiedPlaces);

    }
    @Getter
    private AtomicInteger occupiedPlaces =  new AtomicInteger();
    @Getter
    private AtomicInteger totalCars = new AtomicInteger();
    private final Semaphore semaphore = new Semaphore(5);
    private final Object monitor = new Object();

    public void enter(String carNumber) {

            try {
                System.out.printf("Threads: %s input count: %s \n" ,Thread.currentThread().getName(), semaphore.availablePermits());
                synchronized (monitor) {
                    semaphore.acquire();
                    occupiedPlaces.incrementAndGet();
                    totalCars.incrementAndGet();
                    System.out.println("occupied places: "+occupiedPlaces);
                    Thread.sleep(10);
                    occupiedPlaces.decrementAndGet();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        }
    }
