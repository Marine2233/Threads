package thread.HWThread;

public class TestTread {

    public static void main(String[] args) {
       // showInfoFirstTask();
//        NewThread firstThread = new NewThread("First thread",1,10);
//        NewThread secondThread = new NewThread("Second thread",11,20);
//        NewThread threeThread = new NewThread("Three Thread",21,30);
//        firstThread.start();
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        secondThread.start();
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        threeThread.start();
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

//        DownloadTask runImp1 = new DownloadTask("pict.png",5);
//        DownloadTask runImp2 = new DownloadTask("arch.zip",3);
//        DownloadTask runImp3 = new DownloadTask("movie.mp4",10);
//
//        new Thread(runImp1).start();
//        new Thread(runImp2).start();
//        new Thread(runImp3).start();

//        PrinterTask printerTask = new PrinterTask();
//
//        new Thread(printerTask).start();
//        new Thread(printerTask).start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                System.out.println(i+" ");
            }
        }).start();
        new Thread(() -> {
            for (char i = 'A'; i <'Z' ; i++) {
                System.out.println(i+" ");
            }
        }).start();

    }
    public static void showInfoFirstTask(){
        /**    Напишите программу, которая получает объект текущего потока и выводит:
         имя потока;
         идентификатор;
         приоритет;
         состояние;
         */
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().threadId());
        System.out.println(Thread.currentThread().getPriority());
        System.out.println(Thread.currentThread().getState());
        Thread.currentThread().setName("Update Thread");
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().isDaemon());
    }
}
