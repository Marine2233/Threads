


package thread.HWSynchronizedAtomicVars.prodCons;

public class BufferMain {
    public static void main(String[] args) {

        BoundedBuffer<Integer>queue = new BoundedBuffer<>(5);

        Thread prod = new Thread(()->{
            try {
                for (int i = 0; i < 20; i++) {
                    queue.put(i);
                        Thread.sleep(100);
                }
                queue.put(-1);
                System.out.println("Producer added last element-> "+(-1));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread cons = new Thread(()->{
            try {
                while (true) {
                    Integer val = queue.remove();
                    System.out.println("Consumer thread val-> " + val);
                    if (val == -1) {
                        System.out.println("Last element: " + val);
                        break;
                    }
                    Thread.sleep(1000);
                }
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        });

        prod.start();
        cons.start();
        try {
            prod.join();
            cons.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
