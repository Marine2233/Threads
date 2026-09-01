package thread.classWork.deadLock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
@Getter
@Setter
@NoArgsConstructor
public class PaymentService {
    private final ReentrantLock lock = new ReentrantLock();
    private long money;
    private Semaphore paySemaphore;

    public PaymentService(long money, Semaphore paySemaphore) {
        this.money = money;
        this.paySemaphore = paySemaphore;
    }
}
