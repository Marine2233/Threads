package thread.CollectionsCuncarrent.model;

import lombok.Getter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
public class RetryTicket implements Delayed {

    private final Ticket ticket;
    private final long readyAt;

    public RetryTicket(Ticket ticket, long readyAt) {
        this.ticket = ticket;
        this.readyAt = readyAt;
    }

    public boolean isReady() {
        return System.currentTimeMillis() >= readyAt;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(readyAt - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this == o){
            return 0;
        }
        if (o instanceof RetryTicket){
            return Long.compare(readyAt,((RetryTicket) o).readyAt);
        }
        return Long.compare(this.getDelay(TimeUnit.MILLISECONDS),o.getDelay(TimeUnit.MILLISECONDS));
    }
}
