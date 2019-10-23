package io.github.dunwu.javacore.jvm.memory;

/**
 * 栈
 * <p>
 * VM Args: -Xss512k
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class StackOOM {

	private volatile int count;

	public static void main(String[] args) {
		StackOOM stackOOM = new StackOOM();
		stackOOM.stackLeakByThread();
	}

	public void stackLeakByThread() {
		while (true) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					StackOOM.this.dontStop();
				}
			});
			thread.start();
		}
	}

	private void dontStop() {
		while (true) {
			System.out.println(++count);
		}
	}

}
