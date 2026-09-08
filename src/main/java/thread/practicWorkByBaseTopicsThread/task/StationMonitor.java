package thread.practicWorkByBaseTopicsThread.task;

import thread.practicWorkByBaseTopicsThread.model.RailWayStation;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

public class StationMonitor implements Runnable{
    private final RailWayStation station;
    private volatile boolean running = true;

    public StationMonitor(RailWayStation station) {
        this.station = station;
    }

    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();

        while (running){

            long[] deadLock = mxBean.findDeadlockedThreads();
            if (deadLock != null){
                ThreadInfo []infos = mxBean.getThreadInfo(deadLock,true,true);
                Arrays.stream(infos).forEach(info->{
                    System.out.println("Ждет блокировку: "+info.getLockName());
                    System.out.println("Держит блокировку: "+info.getLockOwnerName());
                });
            }

            System.out.println("=".repeat(10)+"STATION"+"=".repeat(10));
            System.out.println("Active trains:" + station.getStatistics().getActiveTrains().get());
            System.out.println("Processed: "+station.getStatistics().getProcessedTrains().get());
            System.out.println("Failed: "+station.getStatistics().getFailedTrains().get());
            System.out.println("Passengers: "+station.getStatistics().getTotalPassengers().get());
            System.out.println("Free station permits: "+station.getStationCapacity().availablePermits());
            station.getTracks().forEach(trac -> {
            String overload = trac.isOverloaded()? "Overload" : "";
            System.out.printf("\n%s waiting- %s %s:",trac.getName(),trac.getQueueLength(),overload);
        });
            System.out.println();
            System.out.println("=".repeat(30));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
