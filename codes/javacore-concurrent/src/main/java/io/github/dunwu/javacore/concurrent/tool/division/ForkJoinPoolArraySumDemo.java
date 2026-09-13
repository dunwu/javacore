package io.github.dunwu.javacore.concurrent.tool.division;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolArraySumDemo {

public static void main(String[] args) {
    // 创建一个随机数组
    int[] array = new int[10000];
    for (int i = 0; i < array.length; i++) {
        array[i] = i + 1;
    }

    // 使用 ForkJoinPool 计算
    ForkJoinPool pool = new ForkJoinPool();
    ArraySum task = new ArraySum(array);
    int result = pool.invoke(task);

    System.out.println("数组总和: " + result);

    // 验证结果
    int expected = Arrays.stream(array).sum();
    System.out.println("验证结果: " + expected);
    System.out.println("结果是否正确: " + (result == expected));
}

public static class ArraySum extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 1000; // 阈值，小于此值时直接计算
    private final int[] array;
    private final int start;
    private final int end;

    public ArraySum(int[] array) {
        this(array, 0, array.length);
    }

    private ArraySum(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int length = end - start;

        // 如果任务足够小，直接计算
        if (length <= THRESHOLD) {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }

        // 否则分解任务
        int mid = start + (end - start) / 2;

        ArraySum leftTask = new ArraySum(array, start, mid);
        ArraySum rightTask = new ArraySum(array, mid, end);

        // 执行子任务
        leftTask.fork();
        int rightResult = rightTask.compute();
        int leftResult = leftTask.join();

        // 合并结果
        return leftResult + rightResult;
    }


}
}
