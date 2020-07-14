package io.github.dunwu.javacore.concurrent.lock;

/**
 * 活锁问题示例
 */
public class LivelockDemo {

    public static void main(String[] args) {
        final Worker worker1 = new Worker("Worker 1 ", true);
        final Worker worker2 = new Worker("Worker 2", true);

        final CommonResource s = new CommonResource(worker1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                worker1.work(s, worker2);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                worker2.work(s, worker1);
            }
        }).start();
    }

    static class CommonResource {

        private Worker owner;

        CommonResource(Worker d) {
            owner = d;
        }

        Worker getOwner() {
            return owner;
        }

        synchronized void setOwner(Worker d) {
            owner = d;
        }

    }

    static class Worker {

        private String name;

        private boolean active;

        Worker(String name, boolean active) {
            this.name = name;
            this.active = active;
        }

        synchronized void work(CommonResource commonResource, Worker otherWorker) {
            while (active) {
                // wait for the resource to become available.
                if (commonResource.getOwner() != this) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    continue;
                }

                // If other worker is also active let it do it's work first
                if (otherWorker.isActive()) {
                    System.out.println(
                        getName() + " : handing over the resource to the worker: " + otherWorker.getName());
                    commonResource.setOwner(otherWorker);
                    continue;
                }

                // now use the commonResource
                System.out.println(getName() + ": working on the common resource");
                active = false;
                commonResource.setOwner(otherWorker);
            }
        }

        private boolean isActive() {
            return active;
        }

        public String getName() {
            return name;
        }

    }

}
