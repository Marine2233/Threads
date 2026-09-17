package thread.ThreadPool;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        Statistic statistic = new Statistic();
        Monitor monitorTask = new Monitor(statistic);
        MeetingRoom meetingRoom1 = new MeetingRoom(1,"MR-1",false);
        MeetingRoom meetingRoom2 = new MeetingRoom(2,"MR-2",false);
        CountDownLatch latch = new CountDownLatch(1);
        Employee employee = new Employee(meetingRoom1,meetingRoom2,statistic,latch);
        Employee employee2 = new Employee(meetingRoom2,meetingRoom1,statistic,latch);

        ThreadPoolExecutor poolExecutor =
                new ThreadPoolExecutor(
                        2,4,20, TimeUnit.SECONDS,new ArrayBlockingQueue<>(4)
                );
        poolExecutor.execute(monitorTask);

            poolExecutor.execute(employee);
            poolExecutor.execute(employee2);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        latch.countDown();


        poolExecutor.shutdown();
        monitorTask.stop();

        if (!poolExecutor.isTerminating()){
            poolExecutor.shutdownNow();
        }
    }
}
