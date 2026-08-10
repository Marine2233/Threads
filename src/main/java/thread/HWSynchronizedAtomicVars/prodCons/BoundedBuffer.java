package thread.HWSynchronizedAtomicVars.prodCons;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<T> {
    private Queue<T> queue;

    private int capacity;
    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    public BoundedBuffer(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new RuntimeException("Память должна быть > 0");
        }
        queue = new LinkedList<>();
    }

    public void put(T val) {
        lock.lock();
        try{
            while (capacity == queue.size()){
                producer.await();
            }
            queue.add(val);
            System.out.println("Producer added element-> " + val);

        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        lock.unlock();

    }

    public T remove() {
       lock.lock();
       try{
           while (queue.isEmpty()){
               consumer.await();
               System.out.println("Consumer await.");

           }
           T el = queue.remove();
           producer.signal();
           System.out.println("Consumer called producer.");
           return el;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
           lock.unlock();
        }
    }


    public int getCapacity() {
        boolean isLock = false;

        try {
            isLock = lock.tryLock(100,TimeUnit.MILLISECONDS);
            if (isLock){
                return capacity;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if (isLock){
                lock.unlock();
            }
        }
        return -1;
    }
}
