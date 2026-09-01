package io.github.dunwu.javacore.util;

/**
 * 示例：Runtime.exec() 返回 Process 对象，可以在稍后销毁该进程。
 */
public class RuntimeDemo03 {

    /**
     * 演示启动本机程序 5 秒后销毁进程（会短暂弹出记事本窗口）。
     */
    public static void demo() {
        Runtime run = Runtime.getRuntime(); // 取得Runtime类的实例化对象
        Process p = null; // 定义进程变量
        try {
            p = run.exec("notepad.exe"); // 调用本机程序，此方法需要异常处理
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
        }
        try {
            Thread.sleep(5000); // 让此线程存活5秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (p != null) {
            p.destroy(); // 结束此进程
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
