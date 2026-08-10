package thread.classWork;

public class Main {
    public static void main(String[] args) {

        Thread high =
                new Thread(() -> {

                    for(int i=0;i<10;i++){

                        System.out.println("HIGH");

                    }

                });

        Thread low =
                new Thread(() -> {

                    for(int i=0;i<10;i++){

                        System.out.println("LOW");

                    }

                });

        high.setPriority(10);

        low.setPriority(1);

        high.start();

        low.start();

//        Printer printer = new Printer();
//
//        Thread t1 =
//                new Thread(printer::print, "Поток 1");
//
//        Thread t2 =
//                new Thread(printer::print, "Поток 2");
//
//        t1.start();
//
//        t2.start();
//        MyThread thread = new MyThread();
//        thread.start();
//        MyThread tread1 = new MyThread();
//        tread1.setName("First Thread");
//        MyThread second = new MyThread();
//        second.setName("Second Thread");
//        System.out.println(tread1.getName());
//        System.out.println(second.getName());
//        tread1.start();
//        second.start();
//
//
//        System.out.println(Thread.currentThread().getName());
//        System.out.println(Thread.currentThread().getPriority());
//        System.out.println(Thread.currentThread().getState());
//        System.out.println(Thread.currentThread().threadId());
//        System.out.println(Thread.currentThread().isDaemon());


//        TestTask downloadTask =
//                new TestTask(
//                        "Загрузка файла",
//                        5
//                );
//
//        TestTask saveTask =
//                new TestTask(
//                        "Сохранение файла",
//                        5
//                );
//
//        Thread downloadThread =
//                new Thread(
//                        downloadTask,
//                        "Downloader"
//                );
//
//        Thread saveThread =
//                new Thread(
//                        saveTask,
//                        "Saver"
//                );
//
//        downloadThread.start();
//        saveThread.start();
////        Thread thread =
//                Thread.currentThread();
//
//        System.out.println(
//                "Имя: " + thread.getName()
//        );
//
//        System.out.println(
//                "Идентификатор: "
//                        + thread.threadId()
//        );
//
//        System.out.println(
//                "Состояние: "
//                        + thread.getState()
//        );
//
//        System.out.println(
//                "Приоритет: "
//                        + thread.getPriority()
//        );
//
//        System.out.println(
//                "Демон: "
//                        + thread.isDaemon()
//        );
//    }
    }
}
