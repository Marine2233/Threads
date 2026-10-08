package thread.parcelCentre.model;

import thread.parcelCentre.enums.ParcelStatus;

import java.util.concurrent.BlockingQueue;

public class ParcelConsumer implements Runnable{
    private final String name;
    private final BlockingQueue<Parcel> queue;
    private final EventJournal journal;
    private final EmployeeRegistry employeeRegistry;
    private final ParcelStatistics statistics;

    public ParcelConsumer(EmployeeRegistry employeeRegistry,
                          String name,
                          BlockingQueue<Parcel> queue,
                          EventJournal journal,
                          ParcelStatistics statistics) {
        this.employeeRegistry = employeeRegistry;
        this.name = name;
        this.queue = queue;
        this.journal = journal;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            boolean isSorted = false;
            boolean isProcess = false;
            try {
                Parcel parcel = queue.take();
                if (parcel != null){

                    isProcess = parcel.changeStatus(ParcelStatus.WAITING,ParcelStatus.PROCESSING);

                    if (isProcess) {
                        statistics.incProcessing();
                        journal.addEvent("Parcel " + parcel.getId() + " in processing.");
                        Thread.sleep(100);

                        isSorted = parcel.changeStatus(ParcelStatus.PROCESSING, ParcelStatus.SORTED);

                        if (isSorted) {
                            statistics.incSorted();
                            journal.addEvent("Parcel " + parcel.getId() + " Finish sorted.");
                            employeeRegistry.notifyEmployees(parcel);
                        }
                    }

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }finally {
                if (isProcess){
                    statistics.decProcessing();
                }

            }
        }
    }
}
