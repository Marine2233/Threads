package thread.HWSynchronizedAtomicVars.counter;

import java.util.concurrent.atomic.AtomicInteger;

public interface Counter {

    void inc();
    void dec();
    int getValue();
}
