package thread.parcelCentre.repository;

import thread.parcelCentre.model.Parcel;
import thread.parcelCentre.enums.ParcelStatus;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ParcelRepository {
    private final ConcurrentHashMap<Long, Parcel> parcels;
    private final AtomicInteger countParcels = new AtomicInteger();

    public ParcelRepository() {
        this.parcels = new ConcurrentHashMap<>();
    }

    public boolean save(Parcel parcel){
       Parcel el = parcels.putIfAbsent(parcel.getId(), parcel);
       countParcels.incrementAndGet();
       return el == null;
    }

    public Parcel find (long id){
        return parcels.values().stream().filter(parcel -> parcel.getId() == id).findAny().orElse(null);
    }

    public int size(){
        return countParcels.get();
    }

   public long countByStatus(ParcelStatus status){
        return parcels.values().stream().filter(parcel -> parcel.getStatus().equals(status)).count();
    }


    public Map<Long, Parcel> snapshot(){
        return Map.copyOf(parcels);
    }

    public Map<ParcelStatus, Long> getStatusCounts(){
        return parcels.values().stream().
                filter(Objects::nonNull).
                collect(Collectors.groupingBy(Parcel::getStatus,Collectors.counting()));
    }

}
