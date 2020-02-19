package io.github.dunwu.javacore.concurrent.thread;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/11
 */
public class ThreadWaitNotifyDemo {

    public static void main(String[] args) {
        Message msg = new Message("process it");
        Waiter waiter = new Waiter(msg);
        new Thread(waiter, "waiter").start();

        Waiter waiter1 = new Waiter(msg);
        new Thread(waiter1, "waiter1").start();

        Notifier notifier = new Notifier(msg);
        new Thread(notifier, "notifier").start();
        System.out.println("All the threads are started");
    }

    static class Message {

        private String msg;

        Message(String str) {
            this.msg = str;
        }

        String getMsg() {
            return msg;
        }

        void setMsg(String str) {
            this.msg = str;
        }

    }

    static class Waiter implements Runnable {

        private Message msg;

        Waiter(Message m) {
            this.msg = m;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            synchronized (msg) {
                try {
                    System.out.println(name + " waiting to get notified at time:" + System.currentTimeMillis());
                    msg.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + " waiter thread got notified at time:" + System.currentTimeMillis());
                // process the message now
                System.out.println(name + " processed: " + msg.getMsg());
            }
        }

    }

    static class Notifier implements Runnable {

        private Message msg;

        Notifier(Message msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println(name + " started");
            try {
                Thread.sleep(1000);
                synchronized (msg) {
                    msg.setMsg(name + " Notifier work done");
                    msg.notify();
                    // msg.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
