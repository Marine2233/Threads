package thread.classWork;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//        MyThread thread = new MyThread();
//        thread.start();
        MyThread tread1 = new MyThread();
        tread1.setName("First Thread");
        MyThread second = new MyThread();
        second.setName("Second Thread");
        System.out.println(tread1.getName());
        System.out.println(second.getName());
        tread1.start();
        second.start();


        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getPriority());
        System.out.println(Thread.currentThread().getState());
        System.out.println(Thread.currentThread().threadId());
        System.out.println(Thread.currentThread().isDaemon());


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
