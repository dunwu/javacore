package io.github.dunwu.javacore.container.map;

import java.util.HashMap;
import java.util.Map;

public class HashMapDemo08 {

    public static void main(String[] args) {
        Map<Person, String> map = null;
        map = new HashMap<Person, String>();
        map.put(new Person("张三", 30), "zhangsan");    // 增加内容
        System.out.println(map.get(new Person("张三", 30)));
    }

    static class Person {

        private String name;

        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public int hashCode() {
            return this.name.hashCode() * this.age;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Person)) {
                return false;
            }
            Person p = (Person) obj;
            return this.name.equals(p.name) && this.age == p.age;
        }

        @Override
        public String toString() {
            return "姓名：" + this.name + "；年龄：" + this.age;
        }

    }

}
