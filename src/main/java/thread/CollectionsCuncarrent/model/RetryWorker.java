package thread.CollectionsCuncarrent.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

public class RetryWorker implements Runnable{
    private final DelayQueue<RetryTicket> retryQueue;
    private final BlockingQueue<Ticket> ticketQueue;
    private final EventJournal journal;
    private final SupportStatistics statistics;

    public RetryWorker(EventJournal journal, DelayQueue<RetryTicket> retryQueue, BlockingQueue<Ticket> ticketQueue, SupportStatistics statistics) {
        this.journal = journal;
        this.retryQueue = retryQueue;
        this.ticketQueue = ticketQueue;
        this.statistics = statistics;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                RetryTicket ticket = retryQueue.take();

                Ticket tick = ticket.getTicket();

                if (tick.getAttempts().get() >= 3) {
                    tick.changeStatus(TicketStatus.FAILED);
                    journal.add(new SupportEvent("failed", tick.getId()));
                    statistics.incFailed();

                } else{
                    tick.changeStatus(TicketStatus.QUEUED);
                    journal.add(new SupportEvent("Queued", tick.getId()));
                    ticketQueue.put(tick);
                    tick.incrementAttempts();
            }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }
    }
}

