// Cooperative thread termination with a volatile field
package io.github.dunwu.javacore.effective.chapter10.item66.fixedstopthread2;

import java.util.concurrent.TimeUnit;

public class StopThread {

    private static volatile boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!stopRequested) { i++; }
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
    }

}
