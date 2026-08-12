package thread.classWork.practic;

import lombok.Getter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BankAccount {
    @Getter
    private final long id;
    @Getter
    private long balance;
    @Getter
    private final Lock lock = new ReentrantLock(true) {
    };
    private final Condition producer = lock.newCondition();
    private final Condition consumer = lock.newCondition();

    public BankAccount(long id, long balance) {
        if (id > 0 && balance > 0) {
            this.id = id;
            this.balance = balance;
        } else throw new RuntimeException("Параметры не могут быть отрицательными.");
    }

    public void deposit(long amount) {
        if (amount < 0) {
            throw new RuntimeException("Сумма должна быть положительной.");
        }

        boolean isLock = false;
        try {
            isLock = lock.tryLock(100, TimeUnit.MILLISECONDS);
            if (isLock) {
                balance += amount;
                System.out.println("Зачисление ср-в: "+amount+"\nБаланс: "+balance);
                consumer.signal();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (isLock) {
                lock.unlock();
            }
        }
    }

    public void withdraw(long amount) {
        boolean isLock = lock.tryLock();
        try {
            if (isLock) {
                while (amount > balance) {
                    try {
                        System.out.println("Недостаточно ср-в. Await");
                        consumer.await();
                        producer.signal();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                balance-=amount;
                System.out.println("Списание: " + amount+ "\nОстаток:"+balance);
            }
        } finally {
            if (isLock) {
                lock.unlock();
            }
        }
    }

    public boolean tryWithdraw(long amount,int second,TimeUnit unit) {
        if (amount <= 0) {
            throw new RuntimeException("Сумма должна быть положительной.");
        }

        boolean locked = false;
        try {
            locked = lock.tryLock(second,unit);

            if (!locked) {
                System.out.println("Счет занят.");
                return false;
            } else {

                if (balance < amount) {
                    System.out.println("Недостаточно ср-в.");
                    return false;
                }

                System.out.println("Баланс = " + balance);
                balance -= amount;
                System.out.println("Успешное снятие:" + amount + "\nОстаток: " + balance);
                return true;
            }
        } catch ( InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Поток прерван.");
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }
    public int getWaitingThreadsCount(){
        return ((ReentrantLock)lock).getQueueLength();
    }

    public boolean is0verloaded(){
        return  getWaitingThreadsCount() > 5;
    }
public String toString(){
        return ""+id;
}
}
