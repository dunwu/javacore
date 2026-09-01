package io.github.dunwu.javacore.util;

/**
 * 示例：Runtime.exec() 调用本机程序（此处以 Windows 记事本为例，会弹出窗口，请手动关闭）。
 */
public class RuntimeDemo02 {

    /**
     * 演示调用本机程序。
     */
    public static void demo() {
        Runtime run = Runtime.getRuntime(); // 取得Runtime类的实例化对象
        try {
            run.exec("notepad.exe"); // 调用本机程序，此方法需要异常处理
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
