package thread.HWSynchronizedAtomicVars.bankOperation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@ToString
public class Account {
    @Getter
    private long id;
    @Setter
    private volatile BigDecimal balance;
    private Lock lock =new  ReentrantLock(){};

    public Account(BigDecimal balance){
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Баланс не может быть отрицательным.");
        }
        this.balance = balance;
        id = ThreadLocalRandom.current().nextLong();
    }

    public void deposit(BigDecimal amount){

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть строго больше нуля.");
        }

        lock.lock();

        try {
            balance = balance.add(amount);
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(BigDecimal amount) {
        lock.lock();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть больше нуля.");
        }

        try {
            if (amount.compareTo(balance) > 0) {
                return false;
            } else {
                balance = balance.subtract(amount);
                return true;
            }
        }finally {
            lock.unlock();
        }
    }

    public BigDecimal getBalance() {
        lock.lock();
        try{
        return balance;
    }finally {
            lock.unlock();
        }
    }
    public Lock getLock(){
            return lock;}
}
