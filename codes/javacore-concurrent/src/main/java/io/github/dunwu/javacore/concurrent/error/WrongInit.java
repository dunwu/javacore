package io.github.dunwu.javacore.concurrent.error;

import io.github.dunwu.javacore.concurrent.annotation.Error;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误示例：在构造器中启动新线程做字段初始化
 * <p>
 * 问题一（竞态）：{@code new Thread(...).start()} 只是让子线程进入就绪状态，并不保证它在构造器返回前执行完。
 * 主线程紧接着读取 {@code students} 时，子线程很可能还没给 {@code students} 赋值，于是拿到 {@code null} 并抛出
 * {@link NullPointerException}。注意这个异常<b>不是每次必现</b>——机器越快、主线程被调度得越晚，就越可能「碰巧」成功，
 * 这正是并发缺陷最难排查的地方。
 * <p>
 * 问题二（可见性）：即使子线程已经赋好值，{@code students} 既不是 {@code volatile} 也没有任何同步措施，
 * 主线程仍可能读到过期的 {@code null}。
 * <p>
 * 正确做法：不要在构造器里启动线程做初始化；若确实需要异步初始化，必须保留线程引用并在读取前 {@code join()}，
 * 或者改用 {@link java.util.concurrent.CountDownLatch} 等显式的「初始化完成」信号。
 * {@code join()} 之所以有效，是因为它建立了 happens-before 关系：子线程中的所有写操作对 join 返回后的主线程可见。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see io.github.dunwu.javacore.concurrent.annotation.Error
 */
@Error
public class WrongInit {

    private Map<Integer, String> students;

    /**
     * 保存初始化线程的引用，以便读取前等待它结束。真实项目里更好的做法是干脆不在构造器中启动线程
     */
    private final Thread initThread;

    public WrongInit() {
        initThread = new Thread(() -> {
            students = new HashMap<>();
            students.put(1, "王小美");
            students.put(2, "钱二宝");
            students.put(3, "周三");
            students.put(4, "赵四");
        });
        initThread.start();
    }

    public Map<Integer, String> getStudents() {
        return students;
    }

    /**
     * 等待初始化线程结束。join 返回后，子线程对 students 的写入对当前线程可见
     */
    public void awaitInit() throws InterruptedException {
        initThread.join();
    }

    public static void demo() throws InterruptedException {
        WrongInit demo = new WrongInit();

        // 反例：不等待初始化线程结束就直接读取，结果取决于线程调度，可能成功也可能抛 NullPointerException
        try {
            System.out.println("未等待初始化线程就读取 students.get(1) = " + demo.getStudents().get(1));
        } catch (NullPointerException e) {
            System.out.println("未等待初始化线程就读取 students.get(1) 抛出 NullPointerException：students 还是 null");
        }

        // 正例：先 join 等待初始化线程结束，再读取，结果稳定
        demo.awaitInit();
        System.out.println("等待初始化线程结束后读取 students.get(1) = " + demo.getStudents().get(1));
        System.out.println("students = " + demo.getStudents());
    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

}
