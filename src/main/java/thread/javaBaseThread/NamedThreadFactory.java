package thread.javaBaseThread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory{
    private AtomicInteger generationId = new AtomicInteger();

    @Override
    public Thread newThread(Runnable runnable){
        int threadNum = generationId.incrementAndGet();
        Thread thread = new Thread(runnable,"File_Worker-"+threadNum);
        return thread;
    }
}
