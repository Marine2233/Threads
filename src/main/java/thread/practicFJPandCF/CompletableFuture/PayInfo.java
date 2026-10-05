package thread.practicFJPandCF.CompletableFuture;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PayInfo {
    private final int userId;
    private final double totalAmount;
    private final int paymentCount;

    public PayInfo(int paymentCount, int userId, double totalAmount) {
        this.paymentCount = paymentCount;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }
    public String toString(){
        return String.format("%s ; payed total: %s, all payment: %s",userId,totalAmount,paymentCount);
    }
}
