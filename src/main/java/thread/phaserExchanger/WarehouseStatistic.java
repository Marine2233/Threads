package thread.phaserExchanger;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
@Getter
public class WarehouseStatistic {
    private AtomicInteger addedProduct = new AtomicInteger();
    private AtomicInteger movedGoods = new AtomicInteger();

    public void addedGoods(){
        addedProduct.incrementAndGet();
    }

    public void movedGoods(){
        movedGoods.incrementAndGet();
    }
}
