package thread.classWork.practic;

import java.util.LinkedList;
import java.util.Random;



public class BankProgram {
    public static void main(String[] args) {

        LinkedList<BankAccount>list = fabricAccounts();
        BankTransferService service = new BankTransferService();
        Random random = new Random();
        LinkedList<Thread>clients = new LinkedList<>();

        Thread loadMonitor = new Thread(new LoadMonitor(list));
        loadMonitor.start();



        for (int i =0; i < 50; i++){
            Thread client = new Thread(()->{
                System.out.println("all amount before-> "+allAmount(list));
                for (int j =0; j < 100; j++){
                    try {
                        long amount = random.nextLong(100,1000);
                        int fIdx = random.nextInt(list.size());
                        int toIdx = random.nextInt(list.size());

                        while (fIdx == toIdx) {
                            toIdx = random.nextInt(list.size());
                        }

                        BankAccount from = list.get(fIdx);
                        BankAccount to = list.get(toIdx);

                        System.out.println(Thread.currentThread().getName()+ " from: "+ from +" to: "+to);
                        service.transfer(from,to,amount);
                        Thread.sleep(10);


                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                System.out.println("all amount after transfer-> "+allAmount(list));




            });
            client.start();

            clients.add(client);
            for (Thread c: clients){
                try {
                    c.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                loadMonitor.join();
                client.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            loadMonitor.interrupt();
            service.getStatistic();
        }




//        BankAccount account100 = new BankAccount(111,100);
//        Thread consumer = new Thread(()->{
//           account100.tryWithdraw(1000,2,TimeUnit.SECONDS);
//           // account100.withdraw(1000);
//        });
//        consumer.start();
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        Thread cashier = new Thread(()->{
//            try {
//                Thread.sleep(Duration.ofSeconds(5));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//             account100.deposit(1500);
//
//        });
//
//        cashier.start();
//
//        try {
//            consumer.join();
//            cashier.join();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//

    }

    public static LinkedList<BankAccount> fabricAccounts() {
        LinkedList<BankAccount> list = new LinkedList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new BankAccount(i, 100_000));
        }
    return list;
    }

     public static long allAmount(LinkedList<BankAccount>list){
        return list.stream().mapToLong(BankAccount::getBalance).sum();
     }
}
