package thread.practicWorkByBaseTopicsThread.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import thread.practicWorkByBaseTopicsThread.model.RailWayStation;
import thread.practicWorkByBaseTopicsThread.model.Trac;
import thread.practicWorkByBaseTopicsThread.model.Train;
import thread.practicWorkByBaseTopicsThread.model.TrainType;

import java.util.concurrent.*;

public class TrainProcessor implements Runnable {
    private final String json;
    private final ObjectMapper objectMapper;
    private final RailWayStation station;
    private final CountDownLatch finishLatch;
    private final CyclicBarrier barrier;

    public TrainProcessor(CountDownLatch finishLatch,
                          String json,
                          ObjectMapper objectMapper,
                          RailWayStation station,
                          CyclicBarrier serviceBarrier) {
        this.finishLatch = finishLatch;
        this.json = json;
        this.objectMapper = objectMapper;
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.station = station;
        this.barrier = serviceBarrier;
    }

    @Override
    public void run() {
        if (json.isBlank()) {
            System.out.println("json is null.");
            return;
        }
        boolean isAcquire = false;
        boolean isLock1 = false;
        boolean isLock2 = false;
        Trac first = null;
        Trac second = null;
        try {
            Train train = objectMapper.readValue(json, Train.class);
            train.startWaiting();
            isAcquire = station.getStationCapacity().tryAcquire(2, TimeUnit.SECONDS);

            if (!isAcquire) {
                train.fail();
                station.getStatistics().failedTrain();
                return;
            }

            if (train.getType() != TrainType.CARGO && train.getType() != TrainType.EMERGENCY) {
                for (Trac trac : station.getTracks()) {
                    if(trac.getLock().tryLock(2, TimeUnit.SECONDS)){
                        isLock1 = true;
                        first = trac;

                        station.getStatistics().theArrivingTrain();
                        train.occupyTrack();
                        train.startService();
                        station.getStatistics().trainProcessed();
                        station.getStatistics().addPassengers(train,120);
                        try {
                            barrier.await(6,TimeUnit.SECONDS);
                        } catch (InterruptedException | TimeoutException | BrokenBarrierException e) {
                            System.out.println("Группа поездов не состоялась.");
                        }
                        train.finishService();
                        train.depart();
                        station.getStatistics().theDepartingTrain();
                        break;
                    }
                    if (!isLock1) {
                        train.fail();
                        station.getStatistics().failedTrain();
                        return;
                    }
                }

            } else {

                for (int i = 0; i < station.getTracks().size()-1; i++) {
                    Trac trac1 = station.getTracks().get(i);
                    Trac trac2 = station.getTracks().get(i+1);

                    Trac minTrac= trac1.getId() < trac2.getId()? trac1:trac2;
                    Trac maxTrac= trac1.getId() > trac2.getId()? trac1:trac2;

                    if (minTrac.getLock().tryLock(2,TimeUnit.SECONDS)){
                        if (maxTrac.getLock().tryLock(2,TimeUnit.SECONDS)){
                            isLock1 = true;
                            isLock2 = true;
                            first = minTrac;
                            second = maxTrac;
                            train.occupyTrack();
                            station.getStatistics().theArrivingTrain();
                            train.startService();
                            station.getStatistics().trainProcessed();
                            Thread.sleep(2000);
                            try {
                                barrier.await(6,TimeUnit.SECONDS);
                            } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {

                            }
                            train.finishService();
                            train.depart();
                            station.getStatistics().theDepartingTrain();
                            break;
                        }else {
                            minTrac.getLock().unlock();
                        }
                   }
                }
                if (!isLock1 || !isLock2) {
                    train.fail();
                    station.getStatistics().failedTrain();
                    return;
                }
            }
        }catch (JsonProcessingException e) {
            station.getStatistics().getFailedTrains().incrementAndGet();
            System.out.println("Ошибка чтения Json.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (isLock1){
                first.getLock().unlock();
            }
            if (isLock2){
                second.getLock().unlock();
            }
            if (isAcquire) {
                station.getStationCapacity().release();
            }
            finishLatch.countDown();
        }
    }
}