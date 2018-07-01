package io.github.dunwu.javacore.object;

/**
 * @author Zhang Peng
 */
public class Person2 {
    private String name; // 声明姓名属性
    private int age; // 声明年龄属性
    private Book book;
    private Person2 child;

    public Person2() {
    }

    public Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void fun(Person2 obj) { // 接收本类的引用
        obj.age = 18;// 直接通过对象调用本类的私有属性
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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setChild(Person2 c) {
        child = c;
    }

    public Person2 getChild() {
        return child;
    }

    public void tell() {
        System.out.println("姓名：" + this.getName() + "，年龄：" + this.getAge());
    }
}
