package thread.practicFJPandCF.FJP;

public class ArrayStatistics {
    private final long sum;
    private final int min;
    private final int max;
    private final long evenCount;

    public ArrayStatistics(long sum, int min, int max, long evenCount) {
        this.sum = sum;
        this.min = min;
        this.max = max;
        this.evenCount = evenCount;
    }

    public static ArrayStatistics merge(ArrayStatistics a, ArrayStatistics b) {
        return new ArrayStatistics(
                a.sum + b.sum,
                Math.min(a.min, b.min),
                Math.max(a.max, b.max),
                a.evenCount + b.evenCount
        );
    }

    @Override
    public String toString() {
        return String.format("Сумма: %s; Мин: %s; Макс: %s; Чётных: %s", sum, min, max, evenCount);
    }
}
