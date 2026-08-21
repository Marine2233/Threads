package thread.classWork.aircraft;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AirportMain {
    public static void main(String[] args) {
        Airport airport = new Airport();
        AirportMonitor monitor = new AirportMonitor(airport);
        Thread startMonitor = new Thread(monitor);
        startMonitor.setDaemon(true);
        startMonitor.start();
        for (int i = 0; i < 100 ; i++) {
            airport.land(aircraft(i,"flight num " + i),2, TimeUnit.MILLISECONDS);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static Aircraft aircraft(int id,String flightNum){

        Random random = new Random();
        int next = random.nextInt(1,4);
        if (next == 1) {

            return new Aircraft(id, flightNum, AircraftType.PASSENGER);
        }else  if (next == 2){
            return new Aircraft(id,flightNum,AircraftType.CARGO);
        }else return new Aircraft(id,flightNum,AircraftType.EMERGENCY);
    }

}
