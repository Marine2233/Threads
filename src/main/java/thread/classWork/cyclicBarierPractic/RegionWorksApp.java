package thread.classWork.cyclicBarierPractic;

import java.util.concurrent.CyclicBarrier;

public class RegionWorksApp {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(4);
        RegionWorker worker = new RegionWorker(barrier);
        for (int i = 0; i < 4; i++) {
            Thread t = new Thread(worker);
            t.start();
        }
    }
}
