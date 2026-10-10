package thread.CollectionsCuncarrent.model;

import lombok.AllArgsConstructor;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class Retry {
    private final DelayQueue<RetryTicket>retryTickets;

    public RetryTicket detRetryTicket()throws InterruptedException{
        return retryTickets.take();
    }
    public void addRetryTicket(RetryTicket retryTicket){
        retryTickets.put(retryTicket);
    }
}
