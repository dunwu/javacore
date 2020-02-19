package io.github.dunwu.javacore.util;

public class RuntimeDemo03 {

    public static void main(String[] args) {
        Runtime run = Runtime.getRuntime(); // 取得Runtime类的实例化对象
        Process p = null; // 定义进程变量
        try {
            p = run.exec("notepad.exe"); // 调用本机程序，此方法需要异常处理
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
            // System.out.println(e) ;
        }
        try {
            Thread.sleep(5000); // 让此线程存活5秒
        } catch (Exception e) {
        }
        p.destroy(); // 结束此进程
    }

}
