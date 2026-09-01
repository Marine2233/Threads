package thread.classWork.deadLock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
@NoArgsConstructor
public class Warehouse {
    private final ReentrantLock lock = new ReentrantLock();
    private int products;


    public Warehouse(int products) {
        this.products = products;

    }
}
