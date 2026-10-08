package thread.parcelCentre.model;

import thread.parcelCentre.enums.ParcelStatus;
import thread.parcelCentre.repository.ParcelRepository;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class ParcelProducer implements Runnable{
    private final String name;
    private final BlockingQueue<Parcel> queue;
    private final ParcelRepository repository;
    private final EventJournal journal;
    private final ParcelStatistics statistics;
    private final AtomicLong idGenerator ;
    private final int parcelCount;

    public ParcelProducer(EventJournal journal,
                          String name,
                          int parcelCount,
                          BlockingQueue<Parcel> queue,
                          ParcelRepository repository,
                          ParcelStatistics statistics,AtomicLong id) {
        this.journal = journal;
        this.name = name;
        this.parcelCount = parcelCount;
        this.queue = queue;
        this.repository = repository;
        this.statistics = statistics;this.idGenerator = id;
    }

    @Override
    public void run() {
        Random random = new Random();
        int count = 0;
        while (!Thread.currentThread().isInterrupted() && count < parcelCount){

            idGenerator.incrementAndGet();
            long id = idGenerator.get();
            double weight = random.nextDouble(0,200);
            String recipient = this.name + "_"+id;

            Parcel parcel = new Parcel(id,recipient,weight);
            boolean isSave = repository.save(parcel);

            if (isSave){
                try {

                    parcel.changeStatus(ParcelStatus.CREATED,ParcelStatus.WAITING);

                    journal.addEvent("parcel "+id+"- registred and waiting sort");

                    statistics.incCreated();

                    queue.put(parcel);

                    count++;

                    Thread.sleep(500);

                } catch (InterruptedException e) {

                    System.out.println("Ошибка добавления в очередь.");
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        }

    }
}
