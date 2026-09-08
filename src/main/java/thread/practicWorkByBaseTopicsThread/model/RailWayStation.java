package thread.practicWorkByBaseTopicsThread.model;

import lombok.Getter;
import thread.practicWorkByBaseTopicsThread.repository.TrainRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;

@Getter
public class RailWayStation {
    private final List<Trac> tracks ;
    private final Semaphore stationCapacity;
    private final StationStatistics statistics;
    private final TrainRepository repository;

    public RailWayStation(Semaphore stationCapacity) {
        tracks = new ArrayList<Trac>();
        tracks.add(new Trac(1));
        tracks.add(new Trac(2));
        tracks.add(new Trac(3));
        tracks.add(new Trac(4));
        tracks.add(new Trac(5));
        tracks.add(new Trac(6));
        this.repository = new TrainRepository();
        this.stationCapacity = stationCapacity;
        this.statistics = new StationStatistics();

    }

    public Trac getLeastLoadedTrac(){
        return tracks.stream()
                .min(Comparator.comparingInt(Trac::getQueueLength))
                .orElse(null);    }
}
