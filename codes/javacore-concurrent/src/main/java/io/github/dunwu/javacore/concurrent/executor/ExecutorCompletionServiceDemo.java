package io.github.dunwu.javacore.concurrent.executor;

import java.util.concurrent.*;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ExecutorCompletionService
 * @since 2022/2/10
 */
public class ExecutorCompletionServiceDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int threadNum = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        for (int i = 0; i < threadNum; i++) {
            completionService.submit(new ExecutorCompletionServiceDemo.Task(i));
        }

        for (int i = 0; i < threadNum; i++) {
            System.out.println(completionService.take().get());
        }
        System.out.println("执行结束");
        executor.shutdown();
    }

    static class Task implements Callable<String> {

        private final int i;

        public Task(int i) {
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            return Thread.currentThread().getName() + "执行完任务：" + i;
        }

    }

}
