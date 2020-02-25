package io.github.dunwu.javacore.container.bean;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private String name;

    private int age;

    private School school;

    private List<Course> allCourses;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Student() {
        this.allCourses = new ArrayList<Course>();
    }

    @Override
    public String toString() {
        return "学生姓名：" + this.name + "；年龄：" + this.age;
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Student setAge(int age) {
        this.age = age;
        return this;
    }

    public School getSchool() {
        return school;
    }

    public Student setSchool(School school) {
        this.school = school;
        return this;
    }

    public List<Course> getAllCourses() {
        return allCourses;
    }

    public Student setAllCourses(List<Course> allCourses) {
        this.allCourses = allCourses;
        return this;
    }

}
