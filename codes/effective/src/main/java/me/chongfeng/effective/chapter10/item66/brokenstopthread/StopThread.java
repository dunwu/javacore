// Broken! - How long would you expect this program to run? - Page 259
package me.chongfeng.effective.chapter10.item66.brokenstopthread;

import java.util.concurrent.TimeUnit;

public class StopThread {
	private static boolean stopRequested;

	public static void main(String[] args) throws InterruptedException {
		Thread backgroundThread = new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while (!stopRequested)
					i++;
			}
		});
		backgroundThread.start();

		TimeUnit.SECONDS.sleep(1);
		stopRequested = true;
	}
}
