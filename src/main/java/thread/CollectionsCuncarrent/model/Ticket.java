package thread.CollectionsCuncarrent.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
@Getter
@ToString
public class Ticket implements Comparable<Ticket>{
    private final long id;
    private final String customer;
    private final String message;
    private final TicketPriority priority;

    private volatile TicketStatus status;
    private final AtomicInteger attempts = new AtomicInteger();

    public Ticket(String customer, long id, String message, TicketPriority priority) {
        this.customer = customer;
        this.id = id;
        this.message = message;
        this.priority = priority;
        status = TicketStatus.CREATED;
    }

    public void incrementAttempts(){
        attempts.incrementAndGet();
    }
    public void changeStatus(TicketStatus status){
        this.status = status;
    }

    @Override
    public int compareTo(Ticket o) {
        return Integer.compare(o.getPriority().ordinal(),this.getPriority().ordinal());
    }
}
