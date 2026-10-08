package thread.CollectionsCuncarrent;

import thread.CollectionsCuncarrent.model.*;
import thread.CollectionsCuncarrent.repository.TicketRepository;
import thread.phaserExchanger.Monitor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SupportCentre {
    public static void main(String[] args) {

        AtomicLong isGen = new AtomicLong();
        SupportStatistics statistics = new SupportStatistics();
        TicketRepository ticketRepository = new TicketRepository();
        EventJournal journal = new EventJournal();
        SubscriberRegistry registry = new SubscriberRegistry();
        DelayQueue<RetryTicket> ticketDelayQueue= new DelayQueue<>();
        BlockingQueue<Ticket> ticketQueuePriority = new PriorityBlockingQueue<>();
        BlockingQueue<Ticket> ticketQueue = new ArrayBlockingQueue<>(50);
        ConcurrentLinkedQueue<RetryTicket> ticketsLinked = new ConcurrentLinkedQueue<>();

        ExecutorService producer = Executors.newVirtualThreadPerTaskExecutor();
        ExecutorService consumer = Executors.newVirtualThreadPerTaskExecutor();
        ExecutorService retry = Executors.newVirtualThreadPerTaskExecutor();
        ScheduledExecutorService demon = Executors.newSingleThreadScheduledExecutor(r->{
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        demon.scheduleWithFixedDelay(new SupportMonitor(journal,ticketRepository,ticketQueue,ticketDelayQueue,statistics,registry),
                0,1,TimeUnit.SECONDS);

        int i = 0;
        int x = 0;
        while (i < 3) {
            producer.submit(new TicketProducer(isGen, "Prod-" + i, ticketQueue, ticketRepository, statistics, 100));
            i++;
        }


        while (x < 5){
            consumer.submit(new TicketConsumer(journal,ticketQueue,ticketDelayQueue,statistics));
            x++;
        }

        retry.submit(new RetryWorker(journal,ticketDelayQueue,ticketQueue,statistics));


        try {

            producer.shutdown();
            producer.awaitTermination(3,TimeUnit.MINUTES);
            consumer.shutdownNow();
            retry.shutdownNow();
            consumer.awaitTermination(2,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
