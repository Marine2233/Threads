package thread.classWork.HWforThreadAndJackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class JsonOrderProcessor {
    private final ObjectMapper objectMapper;
    private final Semaphore processingSlots ;
    private final CountDownLatch finishLatch;
    private final OrderStatistics statistics;
    private final Lock lock = new ReentrantLock();

    private final List<Order> completedOrders = new ArrayList<>();


    public JsonOrderProcessor(CountDownLatch finishLatch,OrderStatistics statistics,ObjectMapper mapper) {
        this.finishLatch = finishLatch;
        this.processingSlots = new Semaphore(4,true);
        this.statistics = statistics;
        this.objectMapper = mapper;
    }

    public void processJson(String json) {
        if (json == null || json.isBlank()) {
            return;
        }

        boolean semaforAcquire = false;
        try {
            semaforAcquire = processingSlots.tryAcquire(2, TimeUnit.SECONDS);
            if (!semaforAcquire){
                statistics.getRejected0rders().incrementAndGet();
                return;
            }

            Order order = objectMapper.readValue(json, Order.class);

            if(order.startProcessing()) {
                Thread.sleep(1000);
                order.complete();

                lock.lock();
                try {
                    completedOrders.add(order);
                    statistics.incrementProcessed();
                    statistics.addRevenue(order.getPrice());
                }finally {
                    lock.unlock();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);

        } catch (JsonProcessingException e) {
            statistics.getRejected0rders().incrementAndGet();
            throw new RuntimeException(e);
        }finally {
            if (semaforAcquire){
                processingSlots.release();
            }
            finishLatch.countDown();
        }
    }

}
