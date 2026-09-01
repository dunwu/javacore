package io.github.dunwu.javacore.container.map;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例：为自定义对象类重写 hashCode/equals 后，内容相同的不同对象可作为同一个 key 命中。
 * 若未重写，两个 new 出来的对象会被视为不同 key，get 返回 null。
 */
public class HashMapDemo08 {

    /** 演示重写 hashCode/equals 的 Person 作为 key 可被新对象命中。 */
    public static void demo() {
        Map<Person, String> map = null;
        map = new HashMap<Person, String>();
        map.put(new Person("张三", 30), "zhangsan");    // 增加内容
        System.out.println(map.get(new Person("张三", 30)));
    }

    public static void main(String[] args) {
        demo();
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
