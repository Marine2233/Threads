package thread.javaBaseThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessApp {
    public static void main(String[] args) {
        List<FileTask>tasks = generatorTasks(15);
        NamedThreadFactory factory = new NamedThreadFactory();
        FileTaskRejectedHandler rejectedHandler = new FileTaskRejectedHandler();

        ThreadPoolExecutor executor1 = new ThreadPoolExecutor(1,
                3,
                4,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                AtomicInteger i = new AtomicInteger();
                thread.setName("Worker - " + i.incrementAndGet());
                return thread;
            }
        }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("Suicide task" + r);
                ProcessingStatistic.incProcessIRejected();

            }
        });
/*
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                4,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5),
                factory,rejectedHandler);
*/
        ScheduledExecutorService demon = Executors.newScheduledThreadPool(1,r->{
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
        demon.scheduleAtFixedRate(new Monitor(),0,1,TimeUnit.SECONDS);



       int i = 0;
       List<Future<FileResult>>futures = new ArrayList<>();
       while (i < tasks.size()){
           FileProcessor processor = new FileProcessor(tasks.get(i));
           Future<FileResult>future = executor1.submit(processor);
           futures.add(future);
           i++;
       }

       executor1.shutdown();
        try {
            if (!executor1.awaitTermination(2, TimeUnit.SECONDS)) {
                System.out.println(executor1.shutdownNow());
                ProcessingStatistic.NOT_STARTED.set(executor1.shutdownNow().size());

            }
        } catch (InterruptedException e) {
            executor1.shutdownNow();
        }
    }
    public static List< FileTask> generatorTasks(int count){
        List<FileTask>tasks = new ArrayList<>();
        for (int i = 0; i < count ; i++) {
            tasks.add(new FileTask("Task-"+i));
        }
        return tasks;
    }
}
