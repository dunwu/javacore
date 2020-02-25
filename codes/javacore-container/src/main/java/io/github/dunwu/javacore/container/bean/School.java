package io.github.dunwu.javacore.container.bean;

import java.util.ArrayList;
import java.util.List;

public class School {

    private String name;

    private List<Student> allStudents;

    public School(String name) {
        this.name = name;
    }

    public School() {
        this.allStudents = new ArrayList<Student>();
    }

    @Override
    public String toString() {
        return "学校名称：" + this.name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getAllStudents() {
        return this.allStudents;
    }

}
