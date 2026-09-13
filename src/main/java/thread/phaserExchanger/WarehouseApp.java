package thread.phaserExchanger;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

public class WarehouseApp {
    public static void main(String[] args) {
        ArrayList<ProductBatch>batchesForWarehouse1 = generateProdBatch(20);
        ArrayList<ProductBatch>batchesForWarehouse2 = generateProdBatch(10);


        Exchanger<ProductBatch>exchanger = new Exchanger<>();
        Random random = new Random();
        int count = random.nextInt(1,5);
        int quantity = random.nextInt(1,5);

        WarehouseStatistic statistic = new WarehouseStatistic();

        WarehouseWorker taskWorker = new WarehouseWorker(statistic,count,batchesForWarehouse1,exchanger);
        WarehouseWorker taskWorker2 = new WarehouseWorker(statistic,quantity,batchesForWarehouse2,exchanger);

        ScheduledExecutorService demonService = Executors.newScheduledThreadPool(2, run->{
            Thread thread1 = new Thread(run);
            thread1.setDaemon(true);
            return thread1;
        });
        demonService.scheduleAtFixedRate(new Monitor(statistic),0,1200, TimeUnit.MICROSECONDS);

        Runnable addedWarehouse = ()->{

            int i = 0;
            taskWorker.getReadLock().lock();
            try {
                i = taskWorker.getBatches().size() + 1;
            }finally {
                taskWorker.getReadLock().unlock();
            }

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1500);
                    taskWorker.getWriteLock().lock();
                    try {
                        ProductBatch newPB = new ProductBatch(++i, "new PB_" + i, i);
                        taskWorker.addBatch(newPB);
                        System.out.println("Add batch to warehouse. " + newPB);
                    }finally {
                        taskWorker.getWriteLock().unlock();
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        ExecutorService serviceThreads = Executors.newCachedThreadPool();
        serviceThreads.submit(taskWorker);
        serviceThreads.submit(taskWorker2);
        serviceThreads.submit(addedWarehouse);

        serviceThreads.shutdown();

        boolean isTerminated = false;
        try {
            isTerminated = serviceThreads.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!isTerminated){
            serviceThreads.shutdownNow();
        }

    }

    public static ArrayList<ProductBatch> generateProdBatch(int count){
        ArrayList<ProductBatch>productBatches = new ArrayList<>();
        for (int i = 0; i < count; i++){
            ProductBatch batch = new ProductBatch("PB- "+i );
            productBatches.add(batch);
        }
        return productBatches;
    }
}
