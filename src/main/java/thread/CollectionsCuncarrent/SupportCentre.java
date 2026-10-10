package thread.CollectionsCuncarrent;

import thread.CollectionsCuncarrent.model.*;
import thread.CollectionsCuncarrent.repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class SupportCentre {
    public static void main(String[] args) {
        BlockingQueue<Ticket>tickets = new ArrayBlockingQueue<>(50);
        DelayQueue<RetryTicket> delayQueue = new DelayQueue<>();

        Retry retryService = new Retry(delayQueue);
        TicketRepository repository = new TicketRepository();
        SupportStatistics statistics = new SupportStatistics();
        EventJournal journal = new EventJournal();
        AtomicLong idGenerator = new AtomicLong(0);

        ExecutorService prodCons = Executors.newVirtualThreadPerTaskExecutor();
        ScheduledExecutorService demon = Executors.newScheduledThreadPool(1);
        demon.scheduleWithFixedDelay(new ProcessMonitor
                (repository,tickets,delayQueue,statistics,journal,new SubscriberRegistry(),50),0,1,TimeUnit.SECONDS);

        RetryWorker retryWorker = new RetryWorker(retryService, tickets);
        prodCons.submit(retryWorker);

        for (int i = 0; i <= 2; i++) {

            TicketConsumer consumer = new TicketConsumer(journal, tickets, "Consumer_" + i, statistics, retryService);
            prodCons.submit(consumer);

        }

        List<CompletableFuture<?>>futures = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {

            CompletableFuture<?> future = CompletableFuture.
                    runAsync(new TicketProducer(tickets,idGenerator,"Producer- " + i,repository,statistics,100),prodCons);
            futures.add(future);
        }

        for (CompletableFuture<?> future: futures){
            future.join();
        }

        prodCons.shutdown();
        demon.shutdown();

        try {
            prodCons.awaitTermination(1200,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!prodCons.isTerminated()){
            prodCons.shutdownNow();
        }

    }
}
