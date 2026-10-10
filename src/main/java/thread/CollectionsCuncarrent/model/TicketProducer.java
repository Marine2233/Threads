package thread.CollectionsCuncarrent.model;

import thread.CollectionsCuncarrent.repository.TicketRepository;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class TicketProducer implements Runnable{

    private final BlockingQueue<Ticket>tickets;
    private final String producerName;
    private final TicketRepository repository;
    private final SupportStatistics statistics;
    private final AtomicLong idGenerator;
    private final int ticketCount;

    public TicketProducer(BlockingQueue<Ticket> tickets,AtomicLong idGenerator,
                          String producerName,
                          TicketRepository repository,
                          SupportStatistics statistics,
                          int ticketCount) {

        this.tickets = tickets;
        this.idGenerator = idGenerator;
        this.producerName = producerName;
        this.repository = repository;
        this.statistics = statistics;
        this.ticketCount = ticketCount;
    }


    @Override
    public void run() {

        Random random = new Random();
        TicketPriority[] priorities = TicketPriority.values();

        for (int i = 0; i < ticketCount; i++) {
            if (Thread.currentThread().isInterrupted()) break;

            String customer = "Customer_";
            long id = idGenerator.incrementAndGet();
            int pr = random.nextInt(0, priorities.length);

            Ticket ticket = new Ticket(customer + id, id, "offer-ticket " + id, priorities[pr]);
            statistics.incCreated();
            statistics.incEvents();
            repository.save(ticket);
            statistics.incEvents();
            ticket.changeStatus(TicketStatus.QUEUED);
            statistics.incEvents();

            try {
                tickets.put(ticket);
                statistics.incEvents();
                Thread.sleep(1200);

            } catch (InterruptedException e) {
                System.out.println("Возникла ошибка добавления в продюсере.");
                Thread.currentThread().interrupt();
                break;
            }

        }
    }
}
