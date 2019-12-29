package io.github.dunwu.javacore.container.bean;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String name;

    private int credit;

    private List<Student> allStudents;

    public Course(String name, int credit) {
        this();
        this.name = name;
        this.credit = credit;
    }

    public Course() {
        this.allStudents = new ArrayList<Student>();
    }

    @Override
    public String toString() {
        return "Course{" +
            "name='" + name + '\'' +
            ", credit=" + credit +
            ", allStudents=" + allStudents +
            '}';
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public int getCredit() {
        return credit;
    }

    public Course setCredit(int credit) {
        this.credit = credit;
        return this;
    }

    public List<Student> getAllStudents() {
        return allStudents;
    }

    public Course setAllStudents(List<Student> allStudents) {
        this.allStudents = allStudents;
        return this;
    }

}
