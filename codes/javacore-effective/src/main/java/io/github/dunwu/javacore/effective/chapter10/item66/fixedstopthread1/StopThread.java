// Properly synchronized cooperative thread termination - Page 261
package io.github.dunwu.javacore.effective.chapter10.item66.fixedstopthread1;

import java.util.concurrent.TimeUnit;

public class StopThread {

    private static boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!stopRequested()) { i++; }
            }
        });
        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        requestStop();
    }

    private static synchronized boolean stopRequested() {
        return stopRequested;
    }

    private static synchronized void requestStop() {
        stopRequested = true;
    }

}
