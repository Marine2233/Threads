package thread.classWork;

public class TestTask implements Runnable{
    private final String message;
    private final int count;

    public TestTask(
            String message,
            int count
    ) {
        this.message = message;
        this.count = count;
    }

    @Override
    public void run() {

        for (int i = 1; i <= count; i++) {

            System.out.println(
                    Thread.currentThread()
                            .getName()
                            + ": "
                            + message
                            + " №"
                            + i
            );
        }
    }
}
