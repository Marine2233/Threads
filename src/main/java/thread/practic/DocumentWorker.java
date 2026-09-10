package thread.practic;

import java.util.concurrent.Phaser;
//методы у документа по работе с ним, в меин создать Фазер(1) ,
// чтобы маин стал тоже участником

public class DocumentWorker implements Runnable {
    private final Phaser phaser;
    private Document document;
    private ProcessingStatistics statistics;

    public DocumentWorker(Phaser phaser, Document document,ProcessingStatistics statistics) {
        this.document = document;
        this.phaser = phaser;
       this.statistics = statistics;
        phaser.register();
    }

    @Override
    public void run() {

        try {
            Thread.sleep(1000);
            document.loaded();
            statistics.incLoaded();
            phaser.arriveAndAwaitAdvance();
            System.out.println("Process loaded done. Phase-1");


            if(!document.isValid(document)){

                phaser.arriveAndDeregister();
                statistics.incFailed();
                return;
            }

            statistics.incValidate();
            phaser.arriveAndAwaitAdvance();
            System.out.println("Process validate done. Phase- 2");

            document.converted();
            statistics.incConv();
            phaser.arriveAndAwaitAdvance();
            System.out.println("Process converted done. Phase-3");

            Thread.sleep(1000);
            document.saved();
            statistics.incSaved();
            System.out.println("Process saved done. Phase-4");
            phaser.arriveAndDeregister();


        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            phaser.arriveAndDeregister();
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
