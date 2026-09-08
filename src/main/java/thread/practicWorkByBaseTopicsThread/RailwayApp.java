package thread.practicWorkByBaseTopicsThread;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import thread.practicWorkByBaseTopicsThread.model.RailWayStation;
import thread.practicWorkByBaseTopicsThread.task.StationMonitor;
import thread.practicWorkByBaseTopicsThread.task.TrainProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class RailwayApp {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        RailWayStation station = new RailWayStation(new Semaphore(6,true));
        CountDownLatch latch = new CountDownLatch(40);
        CyclicBarrier serviceBarrier = new CyclicBarrier(4,()-> System. out. println("Группа поездов завершила обслуживание"));
        List<String> trains = generateJsonList(40,10);
        List<Thread>threads = new ArrayList<>();
        StationMonitor monitorTask = new StationMonitor(station);
        Thread monitor = new Thread(monitorTask);
        monitor.start();

            for (String json : trains) {
               Thread worker = new Thread(new TrainProcessor(latch,json,mapper,station,serviceBarrier));
                threads.add(worker);
                worker.start();
            }


        try {
            latch.await();
            monitorTask.stop();
            monitor.interrupt();
            monitor.join();
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

        public static List<String> generateJsonList(int count, int corruptedPercent) {
            List<String> list = new ArrayList<>();
            Random random = new Random();
            String[] types = {"PASSENGER", "EXPRESS", "CARGO"};

            for (int i = 0; i < count; i++) {
//                if (random.nextInt(100) < corruptedPercent) {
//                    switch (random.nextInt(4)) {
//                        case 0 -> list.add("   ");
//                        case 1 -> list.add("{id: 123, \"number\": \"R-999\", type: PASSENGER,");
//                        case 2 -> list.add("{\"id\":\"ERR\",\"number\":\"R-102\",\"type\":\"PASSENGER\",\"passengersAll\":450,\"status\":\"CREATED\"}");
//                        case 3 -> list.add("{\"id\":777,\"number\":\"R-103\",\"type\":\"EXPRESS\",\"passengersAll\":200,\"status\":\"CREATED\",\"unknownField\":\"x\"}");
//                    }
//                } else {
                    String type = types[random.nextInt(types.length)];
                    int pass = type.equals("CARGO") ? 0 : random.nextInt(150, 600);

                    // Строка сформирована строго по названиям полей вашего класса Train
                    String json = String.format(
                            "{\"id\":%d,\"number\":\"R-%d\",\"type\":\"%s\",\"passengersAll\":%d,\"status\":\"CREATED\"}",
                            random.nextInt(1, 999999), random.nextInt(100, 999), type, pass
                    );
                    list.add(json);
                }
            //}
            return list;
        }
    }