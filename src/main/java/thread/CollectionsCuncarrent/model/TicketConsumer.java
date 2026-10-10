package thread.CollectionsCuncarrent.model;
import lombok.AllArgsConstructor;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
public class TicketConsumer implements Runnable{

    private final EventJournal journal;
    private final BlockingQueue<Ticket>tickets;
    private final String name;
    private final SupportStatistics statistics;
    private final Retry retry;

    @Override
    public void run() {

        Random random = new Random();

        while (!Thread.currentThread().isInterrupted()){

            try {
                Ticket ticket = tickets.take();

                if (ticket.getStatus().equals(TicketStatus.WAITING_RETRY)){
                    statistics.decRetries();
                }

                ticket.changeStatus(TicketStatus.PROCESSING);
                ticket.incrementAttempts();

                statistics.incProcessing();

                journal.add(new SupportEvent(ticket.getId(),"Process_ticket: " +ticket.getId(),System.currentTimeMillis()));

                int resultProcess = random.nextInt(0,101);

                if (resultProcess < 70){

                    ticket.changeStatus(TicketStatus.COMPLETED);
                    statistics.incCompleted();
                    statistics.decProcessing();
                    journal.add(new SupportEvent(ticket.getId(),"Completed ticket- "+ticket.getId(),System.currentTimeMillis()));

                }else if (resultProcess < 90){

                    statistics.decProcessing();
                    ticket.changeStatus(TicketStatus.WAITING_RETRY);
                    statistics.incRetries();

                    long retryAt = System.currentTimeMillis()+3000;
                    retry.addRetryTicket(new RetryTicket(retryAt,ticket));
                    journal.add(new SupportEvent(ticket.getId(),"Retry ticket- "+ticket.getId(),System.currentTimeMillis()));


                }else {

                    ticket.changeStatus(TicketStatus.FAILED);
                    statistics.decProcessing();
                    statistics.incFailed();
                    journal.add(new SupportEvent( ticket.getId(),"Failed "+ticket.getId(),System.currentTimeMillis()));

                }

                Thread.sleep(1000);


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }
    }
}
