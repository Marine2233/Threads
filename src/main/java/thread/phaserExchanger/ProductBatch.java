package thread.phaserExchanger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Random;
@Getter
@ToString
@AllArgsConstructor
public class ProductBatch {
    private final long id;
    private final String product;
    @Getter
    private static int totalProds = 0;
    private int quantity;

    public ProductBatch( String product) {
        Random random = new Random();
        this.id = random.nextInt(0,100);
        this.product = product;
        this.quantity = random.nextInt(1,10);
        totalProds +=quantity;
    }

    public void incQuantity(int count){
        quantity+=count;
    }

    public void minusQuantity(int count){
        quantity -= count;
    }

}
