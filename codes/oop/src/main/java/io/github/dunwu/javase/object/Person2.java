package io.github.dunwu.javase.object;

/**
 * @author Zhang Peng
 */
public class Person2 {
    private String name; // 声明姓名属性
    private int age; // 声明年龄属性

    public Person2() {
    }

    public Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setName(String n) { // 设置姓名
        name = n;
    }

    public void setAge(int a) { // 设置年龄
        if (a >= 0 && a <= 150) { // 加入验证
            age = a;
        }
    }

    public String getName() { // 取得姓名
        return name;
    }

    public int getAge() { // 取得年龄
        return age;
    }

    public void tell() {
        System.out.println("姓名：" + this.getName() + "，年龄：" + this.getAge());
    }
}
