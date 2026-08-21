package thread.classWork.ThreadLessonTwo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadyOrderQueue {

    private final LinkedList<Order>orders = new LinkedList<>();
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();

    public void addOrder(Order order){
        if (order == null){
            return;
        }
        lock.lock();
        try{

            if (order.getStatus().equals(OrderStatus.CREATED)) {
                order.setStatus(OrderStatus.READY);
                orders.add(order);
                notEmpty.signalAll();
            }

    }finally {
            lock.unlock();
        }
    }

    public Order takeOrder(){
        lock.lock();
        try{
            while (orders.isEmpty()){
                notEmpty.await();
            }
            return orders.removeFirst();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    public int sizeReadyOrders() {
        lock.lock();
        try {
            return orders.size();
        } finally {
            lock.unlock();
        }
    }

    public List<Order> copyList() {
        lock.lock();
        try {
            return new LinkedList<>(orders);
        } finally {
            lock.unlock();
        }
    }

}
