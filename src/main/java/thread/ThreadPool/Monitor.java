package thread.ThreadPool;

public class Monitor implements Runnable {
    private Statistic statistic;
    private boolean flag = true;

    public Monitor(Statistic statistic){
        this.statistic = statistic;
    }

    @Override
    public void run() {
        while (flag && !Thread.currentThread().isInterrupted() ) {
            try {
                Thread.sleep(1000);
                if ((statistic.getAttempts().get() - statistic.getSuccessfulBookings().get()) > 3) {
                    System.out.println("Possible live lock");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                flag = false;
                return;
            }

        }
    }

    public void stop() {
        this.flag = false;
    }
}
