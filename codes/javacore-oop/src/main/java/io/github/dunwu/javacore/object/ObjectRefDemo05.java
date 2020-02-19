package io.github.dunwu.javacore.object;

public class ObjectRefDemo05 {

    public static void main(String[] args) {
        Person2 per = new Person2("张三", 30);
        Book bk = new Book("JAVA SE核心开发", 90.0f);
        per.setBook(bk); // 设置两个对象间的关系，一个人有一本书
        bk.setPerson2(per); // 设置两个对象间的关系，一本书属于一个人
        System.out.println("从人找到书 --> 姓名：" + per.getName() + "；年龄：" + per.getAge() + "；书名：" + per.getBook().getTitle()
            + "；价格：" + per.getBook().getPrice()); // 可以通过人找到书
        System.out.println("从书找到人 --> 书名：" + bk.getTitle() + "；价格：" + bk.getPrice() + "；姓名：" + bk.getPerson2().getName()
            + "；年龄：" + bk.getPerson2().getAge()); // 也可以通过书找到其所有人
    }

}
