package thread.practic;

import java.util.concurrent.Phaser;

public class DocumentMonitor implements Runnable {
    private volatile boolean run = true;
    private ProcessingStatistics statistics;
    private Phaser phaser ;

    public DocumentMonitor(Phaser phaser,ProcessingStatistics statistics) {
        this.phaser = phaser;
        this.statistics = statistics;
    }

    public void stop(){
        run = false;
    }

    @Override
    public void run() {

        while (run &&! Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(1000);
                System.out.println("=".repeat(10)+"Monitor Started"+"=".repeat(10));
                System.out.println("Current phase: " + phaser.getPhase());
                System.out.println("All registrate : "+ phaser.getRegisteredParties());
                System.out.println("Arrived parties: " + phaser.getArrivedParties());
                System.out.println("Stay: "+phaser.getUnarrivedParties());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
