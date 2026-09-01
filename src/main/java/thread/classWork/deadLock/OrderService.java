package thread.classWork.deadLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class OrderService {
    private CountDownLatch synchronTask;
    private PaymentService service;
    private Warehouse warehouse;
    private Semaphore semaphore;
    private List<String> operationLog = new ArrayList<>();



    public OrderService(PaymentService service, CountDownLatch synchronTask, Warehouse warehouse,Semaphore semaphore) {
        this.service = service;
        this.synchronTask = synchronTask;
        this.warehouse = warehouse;
        this.semaphore = semaphore;
    }

    public boolean buy(int countProd,long price){
        if(countProd <= 0 || price < 0){
            operationLog.add("Невалидные данные.");
            synchronTask.countDown();
            return false;
        }
        long totalPayed =0;

        try {
           semaphore.acquire();
            service.getLock().lock();
           try {
               warehouse.getLock().lock();
               try {

                   totalPayed = countProd * price;
                   if (service.getMoney() < totalPayed){
                       operationLog.add("Недостаточно ср-в.");
                       System.out.println("Недостаточно ср-в. "+ Thread.currentThread().getName());
                       return false;
                   }
                   if (countProd > warehouse.getProducts()){
                       operationLog.add("Измените кол-во товара в корзине.");
                       return false;
                   }

                   if (warehouse.getProducts() >= countProd && service.getMoney() >= totalPayed) {
                       warehouse.setProducts(warehouse.getProducts() - countProd);
                       service.setMoney(service.getMoney() - totalPayed);
                       operationLog.add("Заказ оплачен, товар в сборке." + Thread.currentThread().getName());
                       System.out.println("Заказ оплачен, товар в сборке." + Thread.currentThread().getName());

                   }
                   return true;


               }finally {
                   warehouse.getLock().unlock();
               }
           }finally {
               service.getLock().unlock();
           }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }finally {
            semaphore.release();
            synchronTask.countDown();
        }

    }
    void refund(int countProds,long price, boolean isBuy){
        if (countProds <= 0 || price <= 0){
            operationLog.add("Невалидные данные");
            synchronTask.countDown();
            return;
        }

        try {
            semaphore.acquire();
            service.getLock().lock();
            try{
                warehouse.getLock().lock();
                try{
                    if (isBuy) {
                        warehouse.setProducts(warehouse.getProducts() + countProds);
                        long totalCost = countProds * price;
                        service.setMoney(service.getMoney() + totalCost);
                        operationLog.add("Возврат осуществлен. Деньги отправлены");
                        System.out.println("Возврат осуществлен. Деньги отправлены. "+Thread.currentThread().getName());
                    }
                }finally {
                    warehouse.getLock().unlock();
                }
            }finally {
                service.getLock().unlock();
            }

            semaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }finally {

            synchronTask.countDown();
        }
    }
}
