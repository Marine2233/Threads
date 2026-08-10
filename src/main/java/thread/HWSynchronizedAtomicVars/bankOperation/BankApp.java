package thread.HWSynchronizedAtomicVars.bankOperation;

import java.math.BigDecimal;

public class BankApp {
    public static void main(String[] args) {
        Account source = new Account(BigDecimal.valueOf(100_000));
        Account target = new Account(BigDecimal.valueOf(100_000));

        BankService service = new BankService();
        Thread thread1 = new Thread(()->{
            for (int i = 0; i < 100000; i++) {
                service.transfer(source,target,BigDecimal.valueOf(10));
            }
        });

        Thread thread2 = new Thread(()->{
            for (int i = 0; i < 100000 ; i++) {
                service.transfer(target,source,BigDecimal.valueOf(10));
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\nСчет source-> "+source.getBalance());
        System.out.println("Счет target->"+target.getBalance());
        BigDecimal allBalance = source.getBalance().add(target.getBalance());
        System.out.println("All balance: "+allBalance);
    }
}
