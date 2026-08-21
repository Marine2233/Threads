package thread.classWork.practic;

import java.util.LinkedList;

public class LoadMonitor implements Runnable {
    private LinkedList<BankAccount> bankAccounts;
    private volatile boolean running = true;
    public LoadMonitor(LinkedList<BankAccount>bankAccountList){
        this.bankAccounts = bankAccountList;
    }

    @Override
    public void run() {
        System.out.println("=".repeat(10) + "Load monitor" + "=".repeat(10));
        System.out.println();

        while (running && !Thread.currentThread().isInterrupted()) {
            for(BankAccount bankAccount : bankAccounts) {
                if (stop()){
                    Thread.currentThread().interrupt();
                    break;
                }
                System.out.println("Account: " + bankAccount.getId());
                System.out.println("Lock queue: " + bankAccount.getWaitingThreadsCount());
                String overload = bankAccount.is0verloaded() ? "Overload" : "OK";
                System.out.println(overload);
                System.out.println();
            }
            stop();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    private boolean stop(){
        return running = false;
    }
}