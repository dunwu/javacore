package io.github.dunwu.javase.concurrent.chapter01;

import org.junit.Test;

/**
 * @author Zhang Peng
 */
public class RunnableDemoTest {
    @Test
    public void test() {
        RunnableDemo runnableDemo = new RunnableDemo("Runnable 线程") ;	 // 实例化对象
        new Thread(runnableDemo).run() ;	// 调用线程主体
        new Thread(runnableDemo).run() ;	// 调用线程主体
        new Thread(runnableDemo).run() ;	// 调用线程主体
    }
}
