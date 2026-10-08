package thread.parcelCentre.model;


import lombok.Getter;
import thread.parcelCentre.enums.ParcelStatus;

public class Parcel {
    @Getter
    private final long id;

    @Getter
    private final String recipient;

    @Getter
    private final double weight;
    private ParcelStatus status;

    public Parcel(long id, String recipient, double weight) {
        this.id = id;
        this.recipient = recipient;
        this.status = ParcelStatus.CREATED;
        if (weight > 0) {
            this.weight = weight;
        }else {
            throw new IllegalArgumentException("Вес посылки не может быть меньше 0.");
        }
    }

    public synchronized ParcelStatus getStatus(){
     return status;
    }

    public synchronized boolean changeStatus(ParcelStatus expected, ParcelStatus next){
        if (status.equals(expected)){
            status = next;
            return true;
        }
        return false;
    }
public synchronized String toString(){
        return String.format("ïd: %s; recipient: %s; weight: %s; status : %s/\n",id,recipient,weight,status);
}

}
