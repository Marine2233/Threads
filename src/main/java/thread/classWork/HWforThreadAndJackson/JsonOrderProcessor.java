package thread.classWork.HWforThreadAndJackson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidNullException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
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
    private final Semaphore processingSlots;
    private final CountDownLatch finishLatch;
    private final OrderStatistics statistics;
    private final Lock lock = new ReentrantLock();

    private final List<Order> completedOrders = new ArrayList<>();


    public JsonOrderProcessor(CountDownLatch finishLatch, OrderStatistics statistics, ObjectMapper mapper) {
        this.finishLatch = finishLatch;
        this.processingSlots = new Semaphore(4, true);
        this.statistics = statistics;
        this.objectMapper = mapper;
    }

    public void processJson(String json) {
            if (json == null || json.isBlank()) {
                finishLatch.countDown();
                return;
            }

            boolean semaforAcquire = false;
            try {
                semaforAcquire = processingSlots.tryAcquire(2, TimeUnit.SECONDS);

                if (!semaforAcquire) {
                    statistics.getRejectedOrders().incrementAndGet();
                    return;
                }

                JsonNode node = objectMapper.readTree(json);
                String type = node.path("type").asText();
                JsonNode data =  node.path("data");

                if ("HEARTBEAT".equalsIgnoreCase(type)) {
                    return;
                }

                if (!"ORDER".equalsIgnoreCase(type) || data.isMissingNode() || data.isNull()) {
                    statistics.incrementRejected();
                    return;
                }

                Order order = objectMapper.treeToValue(data, Order.class);

                if (order != null && order.startProcessing()) {
                    Thread.sleep(1000);
                    order.complete();

                    lock.lock();
                    try {
                        completedOrders.add(order);
                        statistics.getProcessedOrders().incrementAndGet();
                        statistics.getTotalRevenue().addAndGet(order.getPrice());

                        try {
                            objectMapper.writeValue(new File("ordersComplete.json"),completedOrders);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    } finally {
                        lock.unlock();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                statistics.getFailedOrders().incrementAndGet();
            } catch (JsonProcessingException e) {
                System.err.println("[Jackson Error] Не удалось распарсить: " + e.getMessage());
                if (e instanceof InvalidNullException){
                    System.out.println("Сбой обработки");
                    statistics.incrementFailed();
                }
                System.out.println("Запрос обработки отклонен.");
                statistics.getRejectedOrders().incrementAndGet();

            } finally {
                if (semaforAcquire) {
                    processingSlots.release();
                }
                finishLatch.countDown();
            }
        }
    }
