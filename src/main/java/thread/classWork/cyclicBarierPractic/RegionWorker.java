package thread.classWork.cyclicBarierPractic;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RegionWorker implements Runnable{
    private Random random = new Random();
    private long[]totalRevenue ;
    private final CyclicBarrier barrier;
    private final Object monitorRevenue = new Object();
    private long[] expenses;
    private final Object monitorExpenses = new Object();
    private int regionSize = 4;
    private long totalProfit;
    private Lock lock = new ReentrantLock();

    public RegionWorker(CyclicBarrier barrier) {

        this.barrier = barrier;
        this.totalRevenue = new long[4];
        this.expenses = new long[4];
    }

    @Override
    public void run() {
            calculateRevenue();
            calculateExpenses();
        System.out.println("Профит  составил: "+calculateProfit());
    }

    public long calculateProfit(){
        lock.lock();
        try{

            long sumExpense = Arrays.stream(expenses).sum();
            long sumRevenue = Arrays.stream(totalRevenue).sum();

            totalProfit = sumRevenue - sumExpense;

            return totalProfit;

        }finally {
            lock.unlock();
        }

    }

    public void calculateExpenses() {
        int idx = 0;

        try {
            Thread.sleep(1000);
            System.out.println("Второй этап: Подсчет расходов.");
            synchronized (monitorExpenses) {
                while (idx < regionSize) {
                    expenses[idx] = random.nextInt(600_000, 10_000_000);
                    idx++;
                }
            }
        }catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        }


            try {
                barrier.await();
                System.out.println("Подсчет расходов окончен.");
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

    public void calculateRevenue(){
        int idx = 0;
        System.out.println("Первый этап: Расчет прибыли.");
        try {
                synchronized (monitorRevenue) {
                    Thread.sleep(2000);
                   while (idx < regionSize){
                       totalRevenue[idx] = random.nextInt(400_000,5_000_000);
                       idx++;
                   }
                }
            } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        try {
            barrier.await();
            System.out.println("Расчет прибыли окончен.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
