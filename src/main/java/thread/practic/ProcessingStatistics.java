package thread.practic;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;
@Getter
public class ProcessingStatistics {
    private AtomicInteger loaded = new AtomicInteger();
    private AtomicInteger validated = new AtomicInteger();
    private AtomicInteger converted =new AtomicInteger();
    private AtomicInteger saved = new AtomicInteger();
    private AtomicInteger failed = new AtomicInteger();

    public void incLoaded(){loaded.incrementAndGet();}

    public void incValidate(){validated.incrementAndGet();}

    public void incConv(){converted.incrementAndGet();}

    public void incSaved(){saved.incrementAndGet();}

    public void incFailed(){failed.incrementAndGet();}

}
