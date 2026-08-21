package thread.classWork.aircraft;

import lombok.Getter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter

public class Airport  {

    private final Semaphore runaway = new Semaphore(3) ;
    private volatile int fuelUsed = 0;
    private Object monitor = new Object();
    private final AtomicInteger landed = new AtomicInteger();
    private final AtomicInteger departed = new AtomicInteger();
    private final AtomicInteger rejected = new AtomicInteger();

    public boolean land(Aircraft aircraft, long timeout, TimeUnit unit) {
        boolean locked = false;

        if (aircraft.getType().equals(AircraftType.PASSENGER)) {
            try {
                locked = runaway.tryAcquire(timeout, unit);
                if (!locked) {
                    rejected.incrementAndGet();
                    return false;
                }
                departed.incrementAndGet();
                landed.incrementAndGet();
                synchronized (monitor) {
                    fuelUsed += 1;
                    return true;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (locked) {
                    runaway.release();
                }
            }
        }

        if (aircraft.getType().equals(AircraftType.CARGO)){
            locked = false;
            try{
                locked = runaway.tryAcquire(2,timeout,unit);
                if (!locked){
                    rejected.incrementAndGet();
                    return false;
                }
                departed.incrementAndGet();
                landed.incrementAndGet();
                synchronized (monitor) {
                    fuelUsed += 1;
                    return true;
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                runaway.release(2);
            }
        }
        if (aircraft.getType().equals(AircraftType.EMERGENCY)){
            try{
                runaway.acquire();
                departed.incrementAndGet();
                landed.incrementAndGet();
                synchronized (monitor) {
                    fuelUsed += 1;
                    return true;
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                runaway.release();
            }
        }
        rejected.incrementAndGet();
        return false;
    }
}
