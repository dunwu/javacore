package io.github.dunwu.javacore.bio.bytes;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 管道流：两个线程通过 PipedOutputStream/PipedInputStream 连接，实现线程间直接通信。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/4/26
 */
public class PipedStreamDemo {

    /** 演示发送线程通过管道向接收线程传输数据。 */
    public static void demo() throws InterruptedException {
        Send s = new Send();
        Receive r = new Receive();
        try {
            s.getPos().connect(r.getPis()); // 连接管道
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread t1 = new Thread(s);
        Thread t2 = new Thread(r);
        t1.start(); // 启动线程
        t2.start(); // 启动线程
        // 等待两个线程执行完毕，保证输出完整（直接运行 main 时可省略）
        t1.join();
        t2.join();
    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    static class Send implements Runnable {

        private PipedOutputStream pos = null;

        Send() {
            pos = new PipedOutputStream(); // 实例化输出流
        }

        @Override
        public void run() {
            String str = "Hello World!!!";
            try {
                pos.write(str.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                pos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 得到此线程的管道输出流
         */
        PipedOutputStream getPos() {
            return pos;
        }

    }

    static class Receive implements Runnable {

        private PipedInputStream pis = null;

        Receive() {
            pis = new PipedInputStream();
        }

        @Override
        public void run() {
            byte[] b = new byte[1024];
            int len = 0;
            try {
                len = pis.read(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                pis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("接收的内容为：" + new String(b, 0, len));
        }

        /**
         * 得到此线程的管道输入流
         */
        PipedInputStream getPis() {
            return pis;
        }

    }

}
