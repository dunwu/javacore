package io.github.dunwu.javacore.concurrent.example;

public class ProducerConsumerDemo02 {

    public static void main(String[] args) {
        Info02 info = new Info02(); // 实例化Info对象
        Producer02 pro = new Producer02(info); // 生产者
        Consumer02 con = new Consumer02(info); // 消费者
        new Thread(pro).start();
        new Thread(con).start();
    }

}

class Info02 {

    // 定义信息类
    private String name = "JAVA"; // 定义name属性

    private String content = "JAVASE"; // 定义content属性

    public synchronized void set(String name, String content) {
        this.setName(name); // 设置名称
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setContent(content); // 设置内容
    }

    public synchronized void get() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName() + " --> " + this.getContent());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

class Producer02 implements Runnable {

    // 通过Runnable实现多线程02
    private Info02 info = null; // 保存Info引用

    public Producer02(Info02 info) {
        this.info = info;
    }

    @Override
    public void run() {
        boolean flag = false; // 定义标记位
        for (int i = 0; i < 50; i++) {
            if (flag) {
                this.info.set("JAVA", "JAVASE"); // 设置名称
                flag = false;
            } else {
                this.info.set("并发", "多线程"); // 设置名称
                flag = true;
            }
        }
    }

}

class Consumer02 implements Runnable {

    private Info02 info = null;

    public Consumer02(Info02 info) {
        this.info = info;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            this.info.get();
        }
    }

}
