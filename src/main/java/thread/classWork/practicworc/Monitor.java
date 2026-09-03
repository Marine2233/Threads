package thread.classWork.practicworc;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class Monitor implements Runnable{
    private volatile boolean  running = true;


    @Override
    public void run() {
        System.out.println("Monitor started");

         ThreadMXBean threadMXBean =
                ManagementFactory.getThreadMXBean();

        while (running && !Thread.currentThread().isInterrupted()) {

            long[] deadlockedThreads =
                    threadMXBean.findDeadlockedThreads();

            if (deadlockedThreads != null) {

                System.out.println("DEADLOCK DETECTED");

                ThreadInfo[] infos =
                        threadMXBean.getThreadInfo(
                                deadlockedThreads,
                                true,
                                true
                        );
                for (ThreadInfo info : infos) {

                    System.out.println(
                            "Поток: " + info.getThreadName()
                    );

                    System.out.println(
                            "Ждёт: " + info.getLockName()
                    );

                    System.out.println(
                            "Блокировку держит: "
                                    + info.getLockOwnerName()
                    );

                    System.out.println();
                }
            }

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            System.out.println("Monitor stopped");
        }
    }
    public void stop(){
        running = false;
    }
}
