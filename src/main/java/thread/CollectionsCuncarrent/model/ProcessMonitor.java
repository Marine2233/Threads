package thread.CollectionsCuncarrent.model;

import lombok.AllArgsConstructor;
import thread.CollectionsCuncarrent.repository.TicketRepository;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProcessMonitor implements Runnable{

    private final TicketRepository repository;
    private final BlockingQueue<Ticket> mainQueue;
    private final DelayQueue<RetryTicket> retryQueue;
    private final SupportStatistics statistics;
    private final EventJournal journal;
    private final SubscriberRegistry subscriberRegistry;
    private final int mainQueueCapacity;

    @Override
    public void run() {

            System.out.println("\n--- SUPPORT CENTER ---\n");
            System.out.printf("Repository: %s%n", repository.size());
            System.out.printf("Main queue: %s / %s%n", mainQueue.size(), mainQueueCapacity);
            System.out.printf("Retry queue: %s%n", retryQueue.size());
            System.out.printf("Created: %s%n", statistics.getCreated().get());
            System.out.printf("Processing: %s%n", statistics.getProcessing().get());
            System.out.printf("Completed: %s%n", statistics.getCompleted().get());
            System.out.printf("Failed: %s%n", statistics.getFailed().get());
            System.out.printf("Retries: %s%n", statistics.getRetries().get());
            System.out.printf("Subscribers: %s%n",subscriberRegistry.getSubscribers().size() );
            System.out.printf("Journal events: %s%n", journal.size());

            System.out.println("Priority:");

            Map<TicketPriority,Long>statisticPriority = mainQueue.stream().collect(Collectors.groupingBy(Ticket::getPriority,Collectors.counting()));

            statisticPriority.forEach((key, value) -> System.out.println(key + "/" + value));

            System.out.println("----------------------");
    }
}
