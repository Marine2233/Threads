package thread.SimpleCF.model;

import lombok.AllArgsConstructor;

import java.util.concurrent.DelayQueue;

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
