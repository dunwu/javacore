package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 创建一个线程池，可以安排命令在给定延迟后运行，或定期执行。
 *
 * @author Zhang Peng
 */
public class ScheduledThreadPoolDemo {

	public static void main(String[] args) {
		delay();
		cycle();
	}

	private static void delay() {
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
		scheduledThreadPool.schedule(new Runnable() {
										 @Override
										 public void run() {
											 System.out.println(Thread.currentThread().getName() + " 延迟 3 秒");
										 }
									 }, 3,
			TimeUnit.SECONDS);
	}

	private static void cycle() {
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
		scheduledThreadPool.scheduleAtFixedRate(
			new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " 延迟 1 秒，每 3 秒执行一次");
				}
			}, 1, 3,
			TimeUnit.SECONDS);
	}

}
