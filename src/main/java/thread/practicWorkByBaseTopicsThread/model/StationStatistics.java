package thread.practicWorkByBaseTopicsThread.model;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class StationStatistics {
   @Getter
   private final AtomicInteger processedTrains = new AtomicInteger(0);
   @Getter
   private final AtomicInteger failedTrains = new AtomicInteger(0);
   @Getter
   private final AtomicInteger activeTrains = new AtomicInteger(0);
   @Getter
   private final AtomicLong totalPassengers = new AtomicLong(0);


    public void theArrivingTrain(){
        activeTrains.incrementAndGet();
    }
    public void theDepartingTrain(){
        activeTrains.decrementAndGet();
    }

    public void trainProcessed(){
        processedTrains.incrementAndGet();
    }

    public void failedTrain(){
        failedTrains.incrementAndGet();
    }

    public void addPassengers(Train train,long count){
        if (train != null && count != 0){
            if (train.getPassengersAll() >= count){
                train.setPassengersAll((int) (train.getPassengersAll() - count));
                totalPassengers.addAndGet(count);
            }
        }
    }

}
