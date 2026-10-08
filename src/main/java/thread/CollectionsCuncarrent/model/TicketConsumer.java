package thread.CollectionsCuncarrent.model;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TicketConsumer implements Runnable {
    private final BlockingQueue<Ticket> queue;
    private final BlockingQueue<RetryTicket> reTry;
    private final EventJournal eventJournal;
    private final SupportStatistics statistics;

    public TicketConsumer(EventJournal eventJournal, BlockingQueue<Ticket> queue, BlockingQueue<RetryTicket> reTry, SupportStatistics statistics) {
        this.eventJournal = eventJournal;
        this.queue = queue;
        this.reTry = reTry;
        this.statistics = statistics;
    }


    @Override
    public void run() {
        Random random = new Random();
        while (!Thread.currentThread().isInterrupted()){
            try {

                Ticket ticket = queue.take();
                if (ticket.getStatus().equals(TicketStatus.CREATED)){
                    statistics.incCreated();
                }

                ticket.changeStatus(TicketStatus.PROCESSING);
                statistics.incProcessing();

                eventJournal.add(new SupportEvent("Process ", ticket.getId()));
                ticket.incrementAttempts();

                Thread.sleep(1000);

                int cpu = random.nextInt(100);
                boolean isSuccess = (cpu >= 0 && cpu < 70);
                boolean isRetry = (cpu >= 70 && cpu < 90);

                if (isSuccess){
                    ticket.changeStatus(TicketStatus.COMPLETED);
                    statistics.incCompleted();
                    eventJournal.add(new SupportEvent("Completed ", ticket.getId()));

                }else if (isRetry){
                    ticket.changeStatus(TicketStatus.WAITING_RETRY);
                    ticket.incrementAttempts();
                    statistics.incRetries();
                    eventJournal.add(new SupportEvent("Waiting Retry ", ticket.getId()));
                    reTry.offer(new RetryTicket(ticket,System.currentTimeMillis()+1500));

                }else {
                    ticket.changeStatus(TicketStatus.FAILED);
                    statistics.incFailed();
                    eventJournal.add(new SupportEvent("Failed ", ticket.getId()));
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
