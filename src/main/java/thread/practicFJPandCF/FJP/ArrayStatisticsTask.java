package thread.practicFJPandCF.FJP;

import java.util.concurrent.RecursiveTask;

public class ArrayStatisticsTask extends RecursiveTask<ArrayStatistics> {

        private final int[] array;
        private final int start;
        private final int end;
        private final int threshold;

    public ArrayStatisticsTask( int[] array, int start, int end, int threshold){
            this.array = array;
            this.start = start;
            this.end = end;
            this.threshold = threshold;
        }

        @Override
        protected ArrayStatistics compute () {

            if ((end - start) <= threshold) {
                return computeSequentially();
            }

            int mid = start + end >>> 1;

            ArrayStatisticsTask leftTask = new ArrayStatisticsTask(array, start, mid, threshold);
            ArrayStatisticsTask rightTask = new ArrayStatisticsTask(array, mid, end, threshold);

            leftTask.fork();
            rightTask.fork();

            ArrayStatistics rightResult = rightTask.join();
            ArrayStatistics leftResult = leftTask.join();

            return ArrayStatistics.merge(leftResult, rightResult);
        }

        private ArrayStatistics computeSequentially() {
            long sum = 0;
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            long evenCount = 0;

            for (int i = start; i < end; i++) {
                int val = array[i];
                sum += val;
                if (val < min) min = val;
                if (val > max) max = val;
                if (val % 2 == 0) evenCount++;
            }

            return new ArrayStatistics(sum, min, max, evenCount);
        }
}
