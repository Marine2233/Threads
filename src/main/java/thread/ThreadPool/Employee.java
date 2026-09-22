package thread.ThreadPool;

import java.util.concurrent.CountDownLatch;

public class Employee implements Runnable{
    private MeetingRoom meetingRoom1;
    private MeetingRoom meetingRoom2;
    private final CountDownLatch latch;
    private Statistic statistic;


    public Employee(MeetingRoom meetingRoom, MeetingRoom meetingRoom2, Statistic statistic, CountDownLatch latch) {
        this.meetingRoom1 = meetingRoom;
        this.meetingRoom2 = meetingRoom2;
        this.statistic = statistic;
        this.latch = latch;
    }

    @Override
    public void run() {

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        while (!Thread.currentThread().isInterrupted()){

            boolean isLock1room = false;
            boolean isLock2room = false;

            try {
                if (meetingRoom1.getLock().tryLock()) {
                    isLock1room = true;
                    if (meetingRoom2.getLock().tryLock()) {
                        isLock2room = true;
                        statistic.incSucc();
                        System.out.println("Done.");
                        break;

                    } else {
                        System.out.println("Пробуем еще.");
                        statistic.incAttempts();
                    }
                }



            } finally {
                if (isLock1room){
                    meetingRoom1.getLock().unlock();
                }
                if (isLock2room){
                    meetingRoom2.getLock().unlock();
                }
            }
        }
    }
}
