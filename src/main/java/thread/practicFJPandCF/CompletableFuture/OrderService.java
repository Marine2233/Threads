package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
@Getter
public class OrderService {
    private ExecutorService service;
    public OrderService(ExecutorService service){
        this.service = service;
    }
    public  CompletableFuture<List<Order>>loadOrders(ExecutorService service, User user){
        return new CompletableFuture<List<Order>>().completeAsync(()->user.getOrders(),service);
    }

}
