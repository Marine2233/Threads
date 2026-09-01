package thread.classWork.deadLock;

import java.util.Random;
import java.util.concurrent.*;

public class DeadLock {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Warehouse warehouse = new Warehouse();
        warehouse.setProducts(100);
        Random random = new Random();
        CyclicBarrier barrier = new CyclicBarrier(3,()-> {
            System.out.println("Раунд завершён");
            System.out.println("Остаток товара:" + warehouse.getProducts());
        });


            for (int i = 0; i < 10; i++){
            Thread thread = new Thread(()->{
                long money = random.nextInt(1000,5000);
                PaymentService servicePay = new PaymentService();
                servicePay.setMoney(money);
                servicePay.setPaySemaphore(semaphore);

                OrderService orderService = new OrderService(servicePay,
                        countDownLatch,warehouse,semaphore);

                int count = random.nextInt(1,4);
                int price = random.nextInt(100,4000);

                boolean isBuy = orderService.buy(count,price);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                orderService.refund(count,price, isBuy);

                try{
                    barrier.await(500,TimeUnit.MILLISECONDS);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {

                }

                countDownLatch.countDown();
            });
            thread.start();
        }
            countDownLatch.await();
            Thread.sleep(2000);
            System.out.println("Threads finished work");
    }

}
