package io.github.dunwu.javacore.annotation.standard;
class Person {
    public String getName() {
        return "getInfo";
    }
};


class Student extends Person {
    /**
     * @title main
     * @description 覆写父类的方法
     * @author victor
     * @date 2016年8月2日
     * @return void
     */
    @Override
    public String getName() {
        return "override getInfo";
    }
};


public class OverrideAnnotationDemo {

    public static void main(String[] args) {
        Person per = new Student();
        System.out.println(per.getName());
    }
};
