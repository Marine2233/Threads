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

    public void incTicketPriority(Ticket ticket){
        priorityStatistics.merge(ticket.getPriority(),1L,Long::sum);
    }

    public void incrementCustomerTickets(String customer){
        ticketsByCustomerStatistic.compute(customer,(k,v)->{
            if (v == null){
               return new AtomicInteger(1);
            }else {
                v.incrementAndGet();
               return v;
            }
        });
    }

    public int countUserCallStatistic(String customName){
       return ticketsByCustomerStatistic.computeIfAbsent(customName,customN-> new AtomicInteger()).incrementAndGet();
    }

    public Map<Long,Ticket>snapshot(){
        return Map.copyOf(tickets);
    }

    public long countByStatus(TicketStatus status){
        return tickets.values().stream().filter(ticket -> ticket.getStatus().equals(status)).count();
    }

    public int size(){
        return tickets.size();
    }

    public Ticket find(long id){
        return tickets.get(id);
    }

    public void save(Ticket ticket){
        tickets.putIfAbsent(ticket.getId(),ticket);
    }

}
