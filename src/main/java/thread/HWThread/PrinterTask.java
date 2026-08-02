package thread.HWThread;

public class PrinterTask implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        for (int i = 0;i <= 10_000; i++){
            System.out.printf("Имя потока: %s; Номер итерации-> %s \n" ,Thread.currentThread().getName(), i);
        }

    }
}
