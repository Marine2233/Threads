package thread.phaserExchanger;

public class Monitor implements Runnable{
    private WarehouseStatistic statistic;
    private boolean isRun = true;

    public Monitor(WarehouseStatistic statistic) {
        this.statistic = statistic;
    }

    @Override
    public void run() {
        System.out.println("=".repeat(10)+"Monitor started"+"=".repeat(10));
        while (isRun && !Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(1000);
                System.out.println("Added goods: " + statistic.getAddedProduct().get());
                System.out.println("Moved goods: "+statistic.getMovedGoods().get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
    public void stop(){
        isRun = false;
    }
}
