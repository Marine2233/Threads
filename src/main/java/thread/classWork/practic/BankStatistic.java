package thread.classWork.practic;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BankStatistic {

    private volatile AtomicInteger successful0perations = new AtomicInteger();
    private  volatile AtomicInteger failed0perations = new AtomicInteger();
    private volatile AtomicInteger interrupted0perations = new AtomicInteger();
    private volatile AtomicLong transferredMoney = new AtomicLong();

    public void registerSuccessfulOperation(int operation){
        successful0perations.addAndGet(operation);
    }

    public void registrationFailOperation(int operation){
        failed0perations.addAndGet(operation);
    }

    public void registrationInterruptedOperation(int operation){
        interrupted0perations.addAndGet(operation);
    }

    public void registrationTransferOperation(int amount){
        transferredMoney.addAndGet(amount);
    }

    public void statistic(){
        System.out.println("Успешных операций: " + successful0perations.get());
        System.out.println("Операций с ошибкой: " + failed0perations.get());
        System.out.println("Прерванных операций: " + interrupted0perations.get());
        System.out.println("Переводов: " + transferredMoney.get());
    }

}
