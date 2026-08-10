package thread.HWSynchronizedAtomicVars.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterApp {

    public static void main(String[] args) throws InterruptedException {


        Thread[]threads =  new Thread[20];
        Counter atomicCounter= new Counter() {
            private AtomicInteger intValue = new AtomicInteger();
            @Override
            public void inc() {
                intValue.incrementAndGet();
            }

            @Override
            public void dec() {
                intValue.decrementAndGet();
            }

            @Override
            public int getValue() {
                return intValue.intValue();
            }
        };
        System.out.println("Atomic counter work: ");
        long start = System.currentTimeMillis();

        work(threads,atomicCounter);

        System.out.println("Expected-> 200_000");
        System.out.println("result work->"+atomicCounter.getValue());
        System.out.printf("Time out work -> %s\n",System.currentTimeMillis() -start);

        Counter notSynchronized = new Counter() {
            private int value = 0;
            @Override
            public void inc() {
                value++;
            }

            @Override
            public void dec() {
                value--;
            }

            @Override
            public int getValue() {
                return value;
            }
        };

        System.out.println("\nNot synchronized work:");
        long start2 =System.currentTimeMillis();

        work(threads,notSynchronized);

        System.out.println("Expected-> 200_000");
        System.out.println("result not synchronized-> " + notSynchronized.getValue());
        System.out.printf("Time out not synchronized-> %s",System.currentTimeMillis() - start2);

        Counter synch = new Counter() {
            private  int v = 0;
            @Override
            public synchronized void inc() {
                v++;
            }

            @Override
            public synchronized void dec() {
                v--;
            }

            @Override
            public synchronized int getValue() {
                return v;
            }
        };
        System.out.println("\n\nSynchronized methods start:");
        long s = System.currentTimeMillis();

        work(threads,synch);

        System.out.println("Expected-> 200_000");
        System.out.println("Result work-> " + synch.getValue());
        System.out.printf("Time out %s",System.currentTimeMillis()-s);
    }

    public static void work(Thread[]threads,Counter counter) throws InterruptedException {
        for (int k = 0; k < threads.length; k++){
            threads[k] = new Thread(()->
            {
                for (int i = 0; i < 10_000 ; i++) {
                    counter.inc();
                }
            });
            threads[k].start();
        }

        for (Thread t:threads){
            t.join();
        }
    }
}
