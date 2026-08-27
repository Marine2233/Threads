package thread.classWork.cyclicBarierPractic;

import java.util.concurrent.CyclicBarrier;

public class RegionWorksApp {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(4);
        for (int i = 0; i < 4; i++) {
            Thread t = new Thread(new RegionWorker(barrier));
            t.start();
        }

    }
}
