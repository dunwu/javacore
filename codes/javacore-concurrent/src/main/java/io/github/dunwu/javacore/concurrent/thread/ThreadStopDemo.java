package io.github.dunwu.javacore.concurrent.thread;

/**
 * {@link Thread#stop} 方法有缺陷，已废弃。
 * <p>
 * 使用 {@link Thread#stop} 停止线程会导致它解锁所有已锁定的监视器（由于未经检查的ThreadDeath异常会在堆栈中传播，这是自然的结果）。
 * 如果先前由这些监视器保护的任何对象处于不一致状态，则损坏的对象将对其他线程可见，从而可能导致任意行为。 stop的许多用法应由仅修改某些变量以指示目标线程应停止运行的代码代替。
 * 目标线程应定期检查此变量，如果该变量指示要停止运行，则应按有序方式从其运行方法返回。如果目标线程等待很长时间（例如，在条件变量上），则应使用中断方法来中断等待。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@Deprecated
@SuppressWarnings("all")
public class ThreadStopDemo {

    public static void main(String[] args) {
        StopThread thread = new StopThread();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 终止线程
        thread.stop();
        // 确保线程终止后，才执行下面的代码
        while (thread.isAlive()) {}
        // 输出两个计数器的最终状态
        thread.print();
    }

    /**
     * 持有两个计数器，run 方法中每次执行都会使计数器自增
     */
    private static class StopThread extends Thread {

        private int i = 0;

        private int j = 0;

        @Override
        public void run() {
            synchronized (this) {
                ++i;
                try {
                    // 模拟耗时操作
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ++j;
            }
        }

        public void print() {
            System.out.println("i=" + i + " j=" + j);
        }

    }

}
