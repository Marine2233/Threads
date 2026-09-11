package thread.phaserExchanger;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Getter
public class WarehouseWorker implements Runnable{

    private ArrayList<ProductBatch> batches;
    private WarehouseStatistic statistic;
    private int quantity;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private final Exchanger<ProductBatch> exchanger;
    private boolean isTrue = true;
    private static int totalPosition = 0;

    public WarehouseWorker(WarehouseStatistic statistic,int quantity,ArrayList<ProductBatch> batches, Exchanger<ProductBatch> exchanger) {
        this.batches = batches;
        this.exchanger = exchanger;
        this.quantity = quantity;
        this.statistic = statistic;
        totalPosition += batches.size();
    }

    @Override
    public void run() {

        System.out.println(totalPosition);
        System.out.println(ProductBatch.getTotalProds());
        int round = 0;

        while (isTrue && !Thread.currentThread().isInterrupted()) {
            List<ProductBatch> copy;

            readLock.lock();
            try {
                copy = new ArrayList<>(batches);
            }finally {
                readLock.unlock();
            }

            for (ProductBatch batch: copy) {

                while (round != 10) {

                    if (batch.getQuantity() >= quantity) {
                        ProductBatch batchForExchange = null;
                        try {
                                if (takeBatch(batch,quantity)) {
                                    statistic.movedGoods();
                                    batchForExchange = new ProductBatch(batch.getId(), batch.getProduct(), quantity);
                                }

                            if (batchForExchange != null){
                                ProductBatch exchangeBatch = exchanger.exchange(batchForExchange,2,TimeUnit.SECONDS);

                                        addBatch(exchangeBatch);
                                        statistic.addedGoods();
                                        System.out.println("\nRound- " + (++round));

                                    System.out.println("Всего позиций: " + totalPosition);
                                    System.out.println("Всего товаров:" + ProductBatch.getTotalProds());
                            }

                        } catch (InterruptedException e) {
                                batch.incQuantity(quantity);
                                addBatch(batch);
                            Thread.currentThread().interrupt();
                            return;

                        } catch (TimeoutException e) {
                                batch.incQuantity(quantity);
                                addBatch(batch);
                            break;
                        }
                    }else break;
                }
                if (round == 10) {
                    stop();
                    return;
                }
            }
        }
    }
    public boolean takeBatch(ProductBatch batch,int quantity){
        writeLock.lock();
        try {
            if (batch.getQuantity() > quantity) {
                batch.minusQuantity(quantity);
                return true;

            } else if (batch.getQuantity() == quantity) {
                batch.minusQuantity(quantity);
                batches.remove(batch);
                System.out.println("Товар закончился.");
                return true;
            }

            return false;
        }finally {
            writeLock.unlock();
        }
    }

    public void addBatch(ProductBatch batch) {
        if (batch == null) {
            return;
        }
        writeLock.lock();
        try {
            if (!batches.contains(batch)) {
                batches.add(batch);
            }else {
                int index = batches.indexOf(batch);
                batches.set(index,batch);
                System.out.println("Возврат на склад.");
            }
        }finally {
            writeLock.unlock();
        }
    }

    public void stop(){
        isTrue = false;
    }

    public List<ProductBatch> getBatches() {
        return batches;
    }
}
