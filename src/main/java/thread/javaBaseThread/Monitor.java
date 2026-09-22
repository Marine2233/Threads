package thread.javaBaseThread;

public class Monitor implements Runnable{

    @Override
    public void run() {

                System.out.println("\n\nСтатистика успешно обработанный файлов: "+ProcessingStatistic.COMPLETED.get());
                System.out.println("Статистика прерванных обработок: "+ProcessingStatistic.INTERRUPTED.get());
                System.out.println("Статистика перехваченных обработок: " +ProcessingStatistic.REJECTED.get());
                System.out.println("Статистика незапущенных задач: " + ProcessingStatistic.NOT_STARTED.get());
    }
}
