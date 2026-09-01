package io.github.dunwu.javacore.net.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * TCP Echo 客户端（单客户端版）：连接 {@link EchoServer}，将键盘输入发送给服务端并打印回应，
 * 输入 "bye" 时退出。
 * <p>
 * 配套示例：先运行 {@link EchoServer}，再运行本类。
 * 注：本示例需要交互式键盘输入与服务端配合运行，不纳入自动化测试。
 */
public class EchoClient {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        Socket client = new Socket("localhost", 8888);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); // 接收输入数据
        BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintStream out = new PrintStream(client.getOutputStream()); // 打印数据流
        boolean flag = true; // 定义标志位
        while (flag) {
            System.out.print("输入信息：");
            String str = input.readLine(); // 接收键盘的输入信息
            out.println(str);
            if ("bye".equals(str)) {
                flag = false;
            } else {
                String echo = buf.readLine(); // 接收返回结果
                System.out.println(echo); // 输出回应信息
            }
        }
        buf.close();
        client.close();
    }

}
