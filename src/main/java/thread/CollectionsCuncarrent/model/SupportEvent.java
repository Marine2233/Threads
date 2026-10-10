package thread.CollectionsCuncarrent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SupportEvent {
    private final long ticketId;
    private final String msg;
    private final long timeStamp;
}
