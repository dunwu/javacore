package io.github.dunwu.javacore.reflect;

class Job {}


@Deprecated
@SuppressWarnings("all")
public final class Person {
    private String name;
    private int age;
    private Job job;

    public Person() {};

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public final static boolean checkPersonInfo(String name, int age) {
        if (null == name || 0 == name.length() || age <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[Person]name = " + this.name + ", age = " + this.age;
    }
}
