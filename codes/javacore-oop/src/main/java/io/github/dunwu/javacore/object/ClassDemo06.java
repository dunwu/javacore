package io.github.dunwu.javacore.object;

public class ClassDemo06 {

    public static void main(String[] args) {
        Person2 per = new Person2();

        // 由于 Person2 中属性设为 private，所以不能在外部访问
        // per.name = "张三";
        // per.age = -30;

        // 可以在外部通过 public 方法间接访问 Person2 的私有属性
        per.setName("张三");
        // Person2 中的 set 方法添加了对值是否有效的检查
        // per.setAge(-30);
        per.setAge(18);
        per.tell();
    }

}
