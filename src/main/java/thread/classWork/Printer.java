package thread.classWork;

public class Printer {
    public synchronized void print() {

        System.out.println(
                Thread.currentThread().getName()
                        + " начал печать");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        System.out.println(
                Thread.currentThread().getName()
                        + " закончил печать");
    }
}
