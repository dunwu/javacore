package io.github.dunwu.javacore.util.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 示例：Timer 和 TimerTask——任务调度类都要继承 TimerTask。
 * <p>
 * Timer 负责调度，TimerTask 定义任务内容；任务在独立线程中执行。
 */
class TimerTaskDemo extends TimerTask {

    /** 演示定时任务：1 秒后开始，每 2 秒打印一次当前时间（会一直运行，直到程序结束）。 */
    public static void demo() {
        Timer timer = new Timer(); // 建立Timer类对象
        TimerTaskDemo mytask = new TimerTaskDemo(); // 定义任务
        timer.schedule(mytask, 1000, 2000); // 设置任务的执行，1秒后开始，每2秒重复
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("当前系统时间为：" + sdf.format(new Date()));
    }

    public static void main(String[] args) {
        demo();
    }

}
