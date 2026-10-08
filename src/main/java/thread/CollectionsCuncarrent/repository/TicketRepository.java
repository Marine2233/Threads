package thread.CollectionsCuncarrent.repository;

import thread.CollectionsCuncarrent.model.Ticket;
import thread.CollectionsCuncarrent.model.TicketPriority;
import thread.CollectionsCuncarrent.model.TicketStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketRepository {

    private final ConcurrentHashMap<Long, Ticket> tickets;
    private final ConcurrentHashMap<String, AtomicInteger> ticketsByCustomerStatistic;
    private final ConcurrentHashMap<TicketPriority, Long> priorityStatistics;

    public TicketRepository() {
        this.tickets = new ConcurrentHashMap<>();
        this.ticketsByCustomerStatistic = new ConcurrentHashMap<>();
        this.priorityStatistics = new ConcurrentHashMap<>();
    }

    public void statisticCountCallUserTicket(String customName){
        ticketsByCustomerStatistic.computeIfAbsent(customName,custom -> new AtomicInteger(0)).incrementAndGet();
    }

    public void incPriorityCount(Ticket ticket){
        priorityStatistics.merge(ticket.getPriority(),1L,Long::sum);
    }

    public void  StatisticCountCallUserTicket(String custom){
        ticketsByCustomerStatistic.compute(custom,(k,v)->{
            if (v == null){
                return new AtomicInteger(1);
            }else {
                v.incrementAndGet();
                return v;
            }
        });
    }

    public void save(Ticket ticket){
        tickets.put(ticket.getId(),ticket);
    }

    public boolean saveIfAbsent(Ticket ticket){
       Ticket old =  tickets.putIfAbsent(ticket.getId(),ticket);
       return old == null;
    }

    public Ticket find(long id){
       return tickets.getOrDefault(id,null);
    }
    public int size(){
        return tickets.size();
    }
    public long countByStatus(TicketStatus status){
        return tickets.values().stream().filter(ticket->ticket.getStatus() == status).count();
    }
    public Map<Long, Ticket> snapshot() {
        return Map.copyOf(tickets);
    }

}
