package thread.practicFJPandCF.fjpTask;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        int[]treeshould = new int []{100,1000,10_000,100_000};
        long[]nums = new long[10_000_000];

        Random random = new Random();
        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int i = 0; i < nums.length -1; i++){
            nums[i] = random.nextInt(-10_000,10_000);
        }

        for (int trSh: treeshould){
            long start = System.currentTimeMillis();
            ArrayStatisticsTask task = new ArrayStatisticsTask(nums,0,nums.length,trSh);
            pool.invoke(task);

            long end = System.currentTimeMillis();

            System.out.println(end - start);
        }
    }
}
