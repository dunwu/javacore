package io.github.dunwu.javacore.concurrent.forkjoin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 利用 ForkJoinPool 实现类似 Hadoop MapReduce 统计词频的功能
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-03
 */
public class ForkJoinPool统计词频 {

    public static void main(String[] args) {
        String[] fc = {
            "hello world",
            "hello me",
            "hello fork",
            "hello join",
            "fork join in world"
        };
        // 创建 ForkJoin 线程池
        ForkJoinPool fjp = new ForkJoinPool(3);
        // 创建任务
        MR mr = new MR(fc, 0, fc.length);
        // 启动任务
        Map<String, Long> result = fjp.invoke(mr);
        // 输出结果
        result.forEach((k, v) ->
            System.out.println(k + ":" + v));
    }

    //MR 模拟类
    static class MR extends RecursiveTask<Map<String, Long>> {

        private String[] fc;
        private int start, end;

        // 构造函数
        MR(String[] fc, int fr, int to) {
            this.fc = fc;
            this.start = fr;
            this.end = to;
        }

        @Override
        protected Map<String, Long> compute() {
            if (end - start == 1) {
                return calc(fc[start]);
            } else {
                int mid = (start + end) / 2;
                MR mr1 = new MR(
                    fc, start, mid);
                mr1.fork();
                MR mr2 = new MR(
                    fc, mid, end);
                // 计算子任务，并返回合并的结果
                return merge(mr2.compute(),
                    mr1.join());
            }
        }

        // 合并结果
        private Map<String, Long> merge(
            Map<String, Long> r1, Map<String, Long> r2) {
            Map<String, Long> result = new HashMap<>();
            result.putAll(r1);
            // 合并结果
            r2.forEach((k, v) -> {
                Long c = result.get(k);
                if (c != null) { result.put(k, c + v); } else { result.put(k, v); }
            });
            return result;
        }

        // 统计单词数量
        private Map<String, Long> calc(String line) {
            Map<String, Long> result = new HashMap<>();
            // 分割单词
            String[] words = line.split("\\s+");
            // 统计单词数量
            for (String w : words) {
                Long v = result.get(w);
                if (v != null) { result.put(w, v + 1); } else { result.put(w, 1L); }
            }
            return result;
        }

    }

}
