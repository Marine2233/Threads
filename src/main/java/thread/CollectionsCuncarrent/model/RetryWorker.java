package thread.CollectionsCuncarrent.model;

import lombok.AllArgsConstructor;

import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
public class RetryWorker implements Runnable {
    private final Retry retry;
    private final BlockingQueue<Ticket> tickets;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                RetryTicket ticket = retry.detRetryTicket();
                Ticket ticket1 = ticket.getTicket();

                if (ticket1.getAttempts().get() >= 3) {
                    ticket1.changeStatus(TicketStatus.FAILED);

                } else {

                    try {
                        ticket1.changeStatus(TicketStatus.QUEUED);
                        tickets.put(ticket1);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                }
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}