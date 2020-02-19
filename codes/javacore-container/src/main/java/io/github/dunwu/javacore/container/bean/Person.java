package io.github.dunwu.javacore.container.bean;

import java.util.Objects;

public class Person implements Comparable<Person> {

    private String name;

    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(Person per) {
        if (this.age > per.age) {
            return 1;
        } else if (this.age < per.age) {
            return -1;
        } else {
            return this.name.compareTo(per.name);    // 调用String中的compareTo()方法
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        Person person = (Person) o;
        return age == person.age &&
            Objects.equals(name, person.name);
    }

    @Override
    public String toString() {
        return "姓名：" + this.name + "；年龄：" + this.age;
    }

}
