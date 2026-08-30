package thread.classWork.HWforThreadAndJackson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ClassApp {
    public static void main(String[] args) {
        List<String> jsons = fabricJsonString();
        CountDownLatch finishLatch = new CountDownLatch(jsons.size());
        OrderStatistics statistics = new OrderStatistics();
        JsonOrderProcessor processor = new JsonOrderProcessor(finishLatch,statistics,ConfigMapper.getMapper());
        OrderMonitor m = new OrderMonitor(processor,statistics);

        Thread monitor = new Thread(m);
        monitor.start();

        ArrayList<Thread>threads = new ArrayList<>();
        jsons.forEach(json->{
            Thread thread = new Thread(()-> {

                processor.processJson(json);
            });
            threads.add(thread);
            thread.start();
        });

        try {
            finishLatch.await();
            m.stop();
            monitor.interrupt();
            monitor.join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<String> fabricJsonString() {
        List<String> jsons = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            int type = i % 6;

            switch (type) {
                case 0:
                    jsons.add(String.format("""
                        {
                            "id": %d,
                            "customer_name": "Клиент_%d",
                            "price": %d,
                            "status": "NEW"
                        }
                        """, i, i, 1000 + (i * 50)));
                    break;

                case 1:
                    jsons.add(String.format("""
                        {
                            "id": %d,
                            "name": "Клиент_%d",
                            "price": "дорого",
                            "status": "NEW"
                        }
                        """, i, i));
                    break;

                case 2:
                    jsons.add(String.format("""
                        {
                            "id": %d,
                            "customer": "Клиент_%d",
                            "price": %d
                        """, i, i, 500 + i));
                    break;

                case 3:
                    jsons.add(String.format("""
                        {
                            "id": %d,
                            "customer": null,
                            "price": %d,
                            "status": "UNKNOWN"
                        }
                        """, i, i, i * 100));
                    break;

                case 4:
                    jsons.add(String.format("""
        {
            "id": %d,
            "customer": "Клиент_%d",
            "price": %d,
            "status": "NEW",
            "promoCode": "SUMMER",
            "source": "mobile"
        }
        """,i,i,i*1000));
                    break;

                case 5:
                    jsons.add(String.format("""
        {
        "type": "ORDER",
        "data": {
            "id": %d,
            "customer": "Анна_%d",
            "price": 2000,
            "status": "NEW"
        }
    }                 
    """, i, i));
            }
        }

        return jsons;
    }
}
