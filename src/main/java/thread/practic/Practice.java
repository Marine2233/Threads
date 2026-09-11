package thread.practic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class Practice {
        public static void main(String[] args) {

            Phaser phaser = new Phaser(1);
            ProcessingStatistics statistics = new ProcessingStatistics();
            List<Document>documents = generateDocuments(10);
            DocumentMonitor m = new DocumentMonitor(phaser,statistics);
            Thread monitor = new Thread(m);
            monitor.start();

            for (Document document: documents){
                Thread thread = new Thread(new DocumentWorker(phaser,document,statistics));
                thread.start();
            }

            phaser.arriveAndDeregister();

            while (!phaser.isTerminated()){
                Thread.onSpinWait();
            }

            m.stop();
            monitor.interrupt();

        }
    public static List<Document> generateDocuments(int count) {
        List<Document> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(new Document("Doc_" + i));
        }
        return list;
    }
}
