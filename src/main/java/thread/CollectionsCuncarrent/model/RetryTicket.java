package thread.CollectionsCuncarrent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
@Getter

public class RetryTicket implements Delayed {
    private final Ticket ticket;
    private final long retryAt;

    public RetryTicket(long retryAt, Ticket ticket) {
        this.retryAt = retryAt;
        this.ticket = ticket;
        ticket.changeStatus(TicketStatus.WAITING_RETRY);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long current =  retryAt -System.currentTimeMillis() ;
        return unit.convert(current,TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this == o){
            return 0;
        }
        if (o instanceof RetryTicket){
            return Long.compare(this.retryAt,((RetryTicket) o).retryAt);
        }
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS),o.getDelay(TimeUnit.MILLISECONDS));
    }
}
