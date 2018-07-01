package io.github.dunwu.javacore.concurrent.chapter02;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Zhang Peng
 * @date 2018/5/22
 */
public class CallableAndFutureDemo {

    public static void main(String[] args) {
        Callable<Integer> callable = () -> new Random().nextInt(100);
        FutureTask<Integer> future = new FutureTask<>(callable);
        new Thread(future).start();
        try {
            Thread.sleep(1000);// 可能做一些事情
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
