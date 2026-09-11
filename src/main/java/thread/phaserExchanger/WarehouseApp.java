package thread.phaserExchanger;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Exchanger;

public class WarehouseApp {
    public static void main(String[] args) {
        ArrayList<ProductBatch>batchesForWarehouse1 = generateProdBatch(20);
        ArrayList<ProductBatch>batchesForWarehouse2 = generateProdBatch(10);


        Exchanger<ProductBatch>exchanger = new Exchanger<>();
        Random random = new Random();
        int count = random.nextInt(1,5);
        int quantity = random.nextInt(1,5);

        WarehouseStatistic statistic = new WarehouseStatistic();

        Thread thread = new Thread(new Monitor(statistic));
        thread.setDaemon(true);
        thread.start();

        WarehouseWorker taskWorker = new WarehouseWorker(statistic,count,batchesForWarehouse1,exchanger);
        WarehouseWorker taskWorker2 = new WarehouseWorker(statistic,quantity,batchesForWarehouse2,exchanger);

        Thread addedWarehouse = new Thread(()->{

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
        });
        addedWarehouse.setDaemon(true);
        addedWarehouse.start();

        Thread worker1 = new Thread(taskWorker);
        Thread worker2 = new Thread(taskWorker2);

        worker1.start();
        worker2.start();

        try {
            worker2.join();
            worker1.join();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
