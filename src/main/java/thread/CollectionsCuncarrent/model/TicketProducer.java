package thread.CollectionsCuncarrent.model;
import lombok.Getter;
import thread.CollectionsCuncarrent.repository.TicketRepository;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class TicketProducer implements Runnable{
    private final String producerName;
    private final BlockingQueue<Ticket> queue;
    private final TicketRepository repository;
    private final SupportStatistics statistics;
    private final AtomicLong idGenerator;
    private final int ticketCount;

    public TicketProducer(AtomicLong idGenerator,
                          String producerName,
                          BlockingQueue<Ticket> queue,
                          TicketRepository repository,
                          SupportStatistics statistics,
                          int ticketCount) {
        this.idGenerator = idGenerator;
        this.producerName = producerName;
        this.queue = queue;
        this.repository = repository;
        this.statistics = statistics;
        this.ticketCount = ticketCount;
    }


    @Override
    public void run() {
        for (int i = 0; i < ticketCount; i++){
            try {
                Random random = new Random();
                TicketPriority[]priorities = TicketPriority.values();

                int priority = random.nextInt(0,priorities.length);

                Thread.sleep(1000);
                long id = idGenerator.incrementAndGet();

                Ticket ticket = new Ticket("Customer_"+id,id,"",priorities[priority]);
                statistics.incCreated();
                repository.saveIfAbsent(ticket);
                ticket.changeStatus(TicketStatus.QUEUED);
                queue.put(ticket);

                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
