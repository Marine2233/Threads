package thread.practicFJPandCF.FJP;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ArrayStatisticsApp {
    public static void main(String[] args) {
        int[] numbers = new int[10_000_000];
        int[] thresholds = {100, 10_000, 100_000, 1_000_000};
        ForkJoinPool pool = new ForkJoinPool();

        Random random = new Random();
        for (int i = 0; i < numbers.length -1; i++) {
            numbers[i] = random.nextInt(-1,100_001);
        }

        for (int threshold : thresholds) {
            LocalTime startTime = LocalTime.now();

            ArrayStatisticsTask task = new ArrayStatisticsTask(numbers, 0, numbers.length, threshold);
            ArrayStatistics result = pool.invoke(task);

            LocalTime endTime = LocalTime.now();
            long duration = Duration.between(startTime,endTime).toMillis();

            System.out.printf("\nThreshold: %s ; Время: %s ms. ; %s",
                    threshold, duration, result);
        }

        pool.shutdown();
    }
}