package thread.practicWorkByBaseTopicsThread.repository;

import thread.practicWorkByBaseTopicsThread.model.Train;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrainRepository {
    private List<Train> trains;
    private Lock lock = new ReentrantLock();

    public TrainRepository() {
        trains = new ArrayList<>();
    }

    public synchronized int size() {
        return trains.size();
    }

    public List<Train> getTrains(){
        return List.copyOf(trains);
    }
    public void save(Train train){
        lock.lock();
        boolean isLock = true;
        try{
            if (train == null){
                return;
            }
            trains.add(train);
        }finally {
            if (isLock){
                lock.unlock();
            }
        }
    }
}
