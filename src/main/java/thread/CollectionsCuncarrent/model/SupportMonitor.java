package thread.CollectionsCuncarrent.model;

import thread.CollectionsCuncarrent.repository.TicketRepository;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;

public class SupportMonitor implements Runnable {
    private final TicketRepository repository;
    private final BlockingQueue<Ticket> mainQueue;
    private final DelayQueue<RetryTicket> retryTickets;
    private final SupportStatistics statistic;
    private final SubscriberRegistry registry;
    private final EventJournal eventJournal;

    public SupportMonitor(EventJournal eventJournal,
                          TicketRepository repository,
                          BlockingQueue<Ticket> mainQueue,
                          DelayQueue<RetryTicket> retryTickets,
                          SupportStatistics statistic,
                          SubscriberRegistry registry) {

        this.eventJournal = eventJournal;
        this.repository = repository;
        this.mainQueue = mainQueue;
        this.retryTickets = retryTickets;
        this.statistic = statistic;
        this.registry = registry;
    }

    @Override
    public void run() {
        System.out.println("=".repeat(20) + "Support centre" + "=".repeat(20));
        System.out.println("Repository: " + repository.size());
        System.out.println(String.format("Main queue: %s / 50 ", mainQueue.size()));
        System.out.println("Retry queue: " + retryTickets.size());
        System.out.println("Created: " + statistic.getCreated());
        System.out.println("Processing: " + statistic.getProcessing());
        System.out.println("Completed: " + statistic.getCompleted());
        System.out.println("Failed: " + statistic.getFailed());
        System.out.println("Retries: " + statistic.getRetries());
        System.out.println("Subscribers: " + registry.getSubscribers().size());

        System.out.println("Journal Events: " + eventJournal.size());

        Map<TicketPriority, Long> map = mainQueue.stream()
                .collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));

        if (map.isEmpty()) {
            System.out.println("No tickets in queue.");
        } else {
            map.forEach((key, value) -> System.out.println(" * " + key + ": " + value));
        }
        System.out.println("=".repeat(50));
    }
}

