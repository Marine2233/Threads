package thread.HWSynchronizedAtomicVars.bankOperation;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class BankService {
    public void transfer(Account source, Account target, BigDecimal count) {

        if (count == null || count.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Сумма должна быть положительной.");
        }

        if (source.getId() == target.getId()){
            throw new RuntimeException("Счета должны отличаться.");
        }

        Account f = source.getId() > target.getId() ? source : target;
        Account s = source.getId() > target.getId() ? target : source;

        boolean isLockF = false;
        boolean isLockS = false;

        try {

            isLockF = f.getLock().tryLock(100, TimeUnit.MILLISECONDS);

            if (isLockF) {

                isLockS = s.getLock().tryLock(100, TimeUnit.MILLISECONDS);

                if (isLockS) {

                    if (source.getBalance().compareTo(count) < 0) {
                        throw new RuntimeException("Недостаточно ср-в.");
                    }

                    BigDecimal beforeBalance = source.getBalance();
                    System.out.printf("\nСчет source: %s\nТекущий баланс: %s\nСписание: %s\n",source.getId(), beforeBalance,count);
                    source.withdraw(count);
                    System.out.println("Остаток -> "+source.getBalance());
                    target.deposit(count);
                    System.out.printf("\nСчет target: %s\nПоступление ср-в.: %s\nОстаток: %s\n",target.getId(), count,target.getBalance());
                }
            }
        } catch (InterruptedException e) {

            throw new RuntimeException(e);

        }finally {
            if (isLockF){
                f.getLock().unlock();
            }
            if (isLockS){
                s.getLock().unlock();
            }
        }
    }
}
