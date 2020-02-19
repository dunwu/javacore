package io.github.dunwu.javacore.util.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 任务调度类都要继承TimerTask
 */
class TimerTaskDemo extends TimerTask {

    public static void main(String[] args) {
        Timer timer = new Timer(); // 建立Timer类对象
        TimerTaskDemo mytask = new TimerTaskDemo(); // 定义任务
        timer.schedule(mytask, 1000, 2000); // 设置任务的执行，1秒后开始，每2秒重复
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("当前系统时间为：" + sdf.format(new Date()));
    }

}
