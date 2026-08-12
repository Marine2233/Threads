package thread.classWork.practic;

import lombok.Getter;

public class BankTransferService {

    private final BankStatistic statistics = new BankStatistic();

    public boolean transfer(BankAccount from, BankAccount to, long amount) throws InterruptedException {
        if (amount <= 0) {
            statistics.registrationFailOperation(1);
            throw new RuntimeException("Сумма должна быть положительной.");
        }

        if (from.getId() == to.getId()) {
            statistics.registrationFailOperation(1);
            throw new RuntimeException("Счета не должны совпадать.");
        }

        BankAccount firstLock = from.getId() > to.getId() ? from : to;
        BankAccount secondLock = from.getId() > to.getId() ? to : firstLock;

        boolean isLockFirst = firstLock.getLock().tryLock();
        boolean isLockSecondLock = secondLock.getLock().tryLock();


        try {
            if (isLockFirst){
                if (isLockSecondLock){
                    if (amount > from.getBalance()){
                        statistics.registrationFailOperation(1);
                        System.out.println("Недостаточно ср-в.");
                        return false;
                    }
                    from.withdraw(amount);
                    to.deposit(amount);
                    statistics.registrationTransferOperation(1);
                    statistics.registerSuccessfulOperation(1);
                    System.out.println("Операция прошла успешно.");
                    return true;
                }
                System.out.println("lock-1 не захвачен операция невозможна, попробуйте позже.");
                firstLock.getLock().unlock();
                statistics.registrationFailOperation(1);
                return false;
            }
            System.out.println("lock-2 не захвачен операция невозможна, попробуйте позже.");
            statistics.registrationFailOperation(1);
            return false;
            
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }finally {
            if (isLockFirst){
                firstLock.getLock().unlock();
            }else if (isLockSecondLock){
                secondLock.getLock().unlock();
            }
        }
    }

    public void getStatistic(){
        statistics.statistic();
    }

}
