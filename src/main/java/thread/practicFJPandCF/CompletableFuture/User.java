package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    private String fullName;
    private int id;
    private List<Order>orders;


    public User(String fullName,int id) {
        this.id = id;
        this.fullName = fullName;
        this.orders = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            orders.add(new Order(i, new DeLiveryInfo(id)));
        }
    }

    public String toString(){
        return String.format("%s ; orders: %s",fullName,orders);
    }
}
