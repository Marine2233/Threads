package thread.practicFJPandCF.fjpTask;

public class ArrayStatistics {
    private final long sum;
    private final long min;
    private final long max;
    private final long positiveCount;
    private final long negativeCount;
    private final long zeroCount;

    public ArrayStatistics(long max, long sum, long min, long positiveCount, long negativeCount, long zeroCount) {
        this.max = max;
        this.sum = sum;
        this.min = min;
        this.positiveCount = positiveCount;
        this.negativeCount = negativeCount;
        this.zeroCount = zeroCount;
    }

    public ArrayStatistics combine(ArrayStatistics other){

        return new ArrayStatistics(
        Math.max(max,other.max),
        this.sum + other.sum,
                Math.min(min,other.min),
                this.positiveCount + other.positiveCount,
                this.negativeCount + other.negativeCount,
                this.zeroCount + other.zeroCount
        );


    }

}
