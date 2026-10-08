package thread.CollectionsCuncarrent.model;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.format.DateTimeFormatter;


@Getter
public class SupportEvent {
    private final long ticketId;
    private final String message;
    private final long timestamp;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SupportEvent(String message, long ticketId) {
        this.message = message;
        this.ticketId = ticketId;
        this.timestamp = System.currentTimeMillis();
    }

    public String getFormattedTimestamp() {
        return FORMATTER.format(Instant.ofEpochMilli(timestamp));
    }

    @Override
    public String toString() {
        return String.format("[%s] Event (Ticket %d): %s",
                getFormattedTimestamp(), ticketId, message);
    }

}
