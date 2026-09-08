package thread.practicWorkByBaseTopicsThread.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor
@Data
public class Train {
    private long id;
    private String number;
    private TrainType type;
    private  int passengersAll;
    private TrainStatus status;

    public Train(TrainType type){
        Random random = new Random();
        id= random.nextInt(1,999999);
        int number = random.nextInt(1,1000);
        this.number = String.format("%s-%s",number,(number-1));
        this.type = type;
        if (type == TrainType.PASSENGER ) {
            passengersAll = 7000;
        } else if (type == TrainType.EXPRESS){
            passengersAll = 5000;
        }else passengersAll = 0;
        status = TrainStatus.CREATED;
    }

    public synchronized boolean startWaiting(){
        if (this.status.equals(TrainStatus.CREATED)){

            this.status = TrainStatus.WAITING;
            return true;
        }return false;
    }

    public synchronized boolean occupyTrack(){
        if (this.status.equals(TrainStatus.WAITING)){
            this.status = TrainStatus.ON_TRAC;
            return true;
        }return false;
    }

    public synchronized void startService(){
        if (this.status.equals(TrainStatus.ON_TRAC)){
            this.status = TrainStatus.SERVICING;
        }
    }

    public synchronized void finishService(){
        if (this.status.equals(TrainStatus.SERVICING)){
            this.status = TrainStatus.DEPARTED;
        }
    }

    public synchronized void depart(){
        if (this.status.equals(TrainStatus.READY_TO_DEPART)){
            this.status = TrainStatus.DEPARTED;
        }
    }

    public synchronized void fail(){
        if (this.status.equals(TrainStatus.DEPARTED)){
            this.status = TrainStatus.FAILED;
        }
    }

}
