package thread.parcelCentre;

import thread.parcelCentre.model.*;
import thread.parcelCentre.repository.ParcelRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ParcelCentreApp {
    public static void main(String[] args) {

        BlockingQueue<Parcel>parcelBlockingQueue = new PriorityBlockingQueue<>(10, Comparator.comparing(Parcel::getWeight).reversed());
        ParcelRepository repository = new ParcelRepository();
        EventJournal journal = new EventJournal();
        EmployeeRegistry employeeRegistry = new EmployeeRegistry();
        ParcelStatistics statistics = new ParcelStatistics();
        AtomicLong idGen = new AtomicLong();

        ExecutorService prodPoll = Executors.newVirtualThreadPerTaskExecutor();
        ExecutorService consumerPool = Executors.newVirtualThreadPerTaskExecutor();

        employeeRegistry.subscribe("Max");
        employeeRegistry.subscribe("Tim");
        employeeRegistry.subscribe("Bart");
        List<CompletableFuture<?>>listProd = new ArrayList<>();
        List<CompletableFuture<?>>listCons = new ArrayList<>();

        int p = 0;
        int c = 0;

        while (c < 2){
            CompletableFuture<?>consFuture = CompletableFuture.runAsync(new ParcelConsumer(employeeRegistry,"Cons_"+c,parcelBlockingQueue,journal,statistics),consumerPool);
            listCons.add(consFuture);
            c++;
        }

        while (p < 3){
            CompletableFuture<?>prodFuture = CompletableFuture.runAsync(
                    new ParcelProducer(journal,"Prod_"+p,20,parcelBlockingQueue,repository,statistics,idGen),prodPoll);
            listProd.add(prodFuture);
            p++;
        }

        boolean isTrueProdsExecutor = false;

        try {

            listProd.forEach(CompletableFuture::join);
            long start = System.currentTimeMillis();

            while (statistics.getSorted().get() != 60 && (System.currentTimeMillis() - start) < 30000){
                Thread.sleep(100);
            }

            if (statistics.getSorted().get() == 60){
                isTrueProdsExecutor = true;
            }

        }catch (CompletionException e){
            throw new RuntimeException("КРИТИЧЕСКАЯ ОШИБКА: Одна из задач завершилась сбоем: " + e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }finally {
            prodPoll.shutdown();
            consumerPool.shutdown();
            try {
                if (!consumerPool.awaitTermination(2,TimeUnit.SECONDS)){
                    consumerPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (!isTrueProdsExecutor){
            throw new RuntimeException("Время обработки закончилось ");
        }

        System.out.println("\n=== ФИНАЛЬНАЯ СТАТИСТИКА ===");
        System.out.println("Всего создано: " + statistics.getCreated());
        System.out.println("Успешно отсортировано: " + statistics.getSorted());
        System.out.println("Осталось в очереди: " + parcelBlockingQueue.size());
        System.out.println("Событий в журнале: " + journal.size());
        int i = 0;
        while (i < 4){
            System.out.println(journal.getNextEvent());
            i++;
        }
        System.out.println("============================");
    }
}
