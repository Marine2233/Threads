package thread.practicFJPandCF.parallelStreem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ParallelStreemSimple {
    public static void main(String[] args) {
        List<Long>nums = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5_000_000; i++){
            long num = random.nextLong(-100_000,100_000);
            nums.add(num);
        }

        long start = System.currentTimeMillis();
        long min = nums.stream().min(Long::compareTo).get();
        long max = nums.stream().max(Long::compareTo).get();
        long sum = nums.stream().mapToLong(Long::longValue).sum();
        long countPositiveNums = nums.stream().filter(num-> num > 0).count();
        long negativeNums =nums.stream().filter(num-> num < 0).count();
        long zero = nums.stream().filter(num-> num == 0).count();
        long sumPositiveCount = nums.stream().filter(num -> num > 0).mapToLong(Long::longValue).sum();
        long end = System.currentTimeMillis();
        System.out.println(min + "\n" + max + "\n" + sum + "\n" + countPositiveNums + "\n" + negativeNums + "\n" +zero + "\n" + sumPositiveCount);
        System.out.println(end - start + " ms.");
        System.out.println();

        long startParallel = System.currentTimeMillis();
        long minparallel = nums.stream().parallel().min(Long::compareTo).get();
        long maxParallel = nums.stream().parallel().max(Long::compareTo).get();
        long sumParallel = nums.stream().parallel().mapToLong(Long::longValue).sum();
        long countPositiveNumsParallel = nums.stream().parallel().filter(num-> num > 0).count();
        long negativeNumsParallel =nums.stream().parallel().filter(num-> num < 0).count();
        long zeroParallel = nums.stream().parallel().filter(num-> num == 0).count();
        long sumPositiveCountParallel = nums.stream().parallel().filter(num -> num > 0).mapToLong(Long::longValue).sum();
        long endParaller = System.currentTimeMillis();
        System.out.print(minparallel + "\n" + maxParallel + "\n" + sumParallel + "\n" + countPositiveNumsParallel
                + "\n" + negativeNumsParallel + "\n" +zeroParallel + "\n" + sumPositiveCountParallel);

        System.out.println(endParaller - startParallel+ " ms.");

    }
}
