package io.github.dunwu.javacore.concurrent.tool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo3 {

    public static void demo() throws InterruptedException, ExecutionException {

        // 创建一个线程池来执行任务
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 创建两个 Callable 对象
        Callable<Integer> t1 = () -> {
            int result = 0;
            for (int i = 1; i <= 100; i++) {
                result += i;
            }
            return result;
        };
        Callable<Integer> t2 = () -> {
            int result = 0;
            for (int i = 101; i <= 200; i++) {
                result += i;
            }
            return result;
        };

        // 创建两个 FutureTask 对象
        FutureTask<Integer> f1 = new FutureTask<>(t1);
        FutureTask<Integer> f2 = new FutureTask<>(t2);

        // 提交任务到线程池执行
        executor.execute(f1);
        executor.execute(f2);

        // 获取任务的结果
        Integer value1 = f1.get();
        Integer value2 = f2.get();
        // 注意：必须给 value1 + value2 加括号。若写成 "total = " + value1 + value2，
        // + 会按从左到右结合，先与字符串拼接，结果是 "505015050" 而不是算术和 20100
        System.out.println("value1 = " + value1 + ", value2 = " + value2);
        System.out.println("total = " + (value1 + value2));

        // 关闭线程池
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        demo();
    }

}
// Output:
// value1 = 5050, value2 = 15050
// total = 20100
