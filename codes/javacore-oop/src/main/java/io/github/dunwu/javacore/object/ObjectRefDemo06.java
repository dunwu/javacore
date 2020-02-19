package io.github.dunwu.javacore.object;

public class ObjectRefDemo06 {

    public static void main(String[] args) {
        Person2 per = new Person2("张三", 30);
        Person2 cld = new Person2("张草", 10); // 定义一个孩子
        Book bk = new Book("JAVA SE核心开发", 90.0f);
        Book b = new Book("一千零一夜", 30.3f);
        per.setBook(bk); // 设置两个对象间的关系，一个人有一本书
        bk.setPerson2(per); // 设置两个对象间的关系，一本书属于一个人
        cld.setBook(b); // 设置对象间的关系，一个孩子有一本书
        b.setPerson2(cld); // 设置对象间的关系，一本书属于一个孩子
        per.setChild(cld); // 设置对象间的关系，一个人有一个孩子
        System.out.println("从人找到书 --> 姓名：" + per.getName() + "；年龄：" + per.getAge() + "；书名：" + per.getBook().getTitle()
            + "；价格：" + per.getBook().getPrice()); // 可以通过人找到书
        System.out.println("从书找到人 --> 书名：" + bk.getTitle() + "；价格：" + bk.getPrice() + "；姓名：" + bk.getPerson2().getName()
            + "；年龄：" + bk.getPerson2().getAge()); // 也可以通过书找到其所有人
        // 通过人找到孩子，并找到孩子所拥有的书
        System.out.println(per.getName() + "的孩子 --> 姓名：" + per.getChild().getName() + "；年龄：" + per.getChild().getAge()
            + "；书名：" + per.getChild().getBook().getTitle() + "；价格：" + per.getChild().getBook().getPrice());
    }

}
