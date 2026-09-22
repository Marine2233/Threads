package thread.javaExecutorService;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class FileTaskRejectedHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        ProcessingStatistic.incProcessIRejected();
        System.out.println("\nЗадача отклонена." + r);
        System.out.println("Size Thread pool: "+executor.getPoolSize()+"\nActive Thread: "+ executor.getActiveCount());

    }
}
