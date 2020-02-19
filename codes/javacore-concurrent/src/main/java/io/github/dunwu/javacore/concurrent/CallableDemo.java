package io.github.dunwu.javacore.concurrent;

import java.util.concurrent.Callable;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
class CallableDemo implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("子线程在进行计算");
        Thread.sleep(3000);
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        return sum;
    }

}
