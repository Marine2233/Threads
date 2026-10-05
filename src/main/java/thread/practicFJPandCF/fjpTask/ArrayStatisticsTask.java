package thread.practicFJPandCF.fjpTask;

import java.util.AbstractList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ArrayStatisticsTask extends RecursiveTask<ArrayStatistics> {
    private final long[] array;
    private final int from;
    private final int to;
    private final int threshold;

    public ArrayStatisticsTask(long[] array, int from, int to, int threshold) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.threshold = threshold;
    }


    @Override
    protected ArrayStatistics compute() {

        if (to-from <= threshold){
            return computeSimple(array);
        }

        int mid = (to + from)>>>1;
        ArrayStatisticsTask first = new ArrayStatisticsTask(array,from,mid,threshold);
        ArrayStatisticsTask twoTask = new ArrayStatisticsTask(array,mid,to,threshold);

        first.fork();
         ArrayStatistics resultTwoTask = twoTask.compute();
         ArrayStatistics resultFirstTask = first.join();

        return resultFirstTask.combine(resultTwoTask);
    }

    private ArrayStatistics computeSimple(long[]array){
        long sum = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long positiveCount = 0;
        long negativeCount = 0;
        long zeroCount = 0;

        for(int i  = from;i< to; i++){
            sum+=array[i];
            if (array[i] > 0){
                positiveCount++;
            }else {
                negativeCount++;
            }
            if (array[i] == 0) {
                zeroCount++;
            }

            if (array[i] < min){
                min = array[i];
            }

            if (array[i] > max){
                max = array[i];
            }

        }
        return new ArrayStatistics(max,sum,min,positiveCount,negativeCount,zeroCount);

    }
}
