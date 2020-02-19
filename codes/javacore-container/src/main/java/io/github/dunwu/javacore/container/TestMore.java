package io.github.dunwu.javacore.container;

import io.github.dunwu.javacore.container.bean.Course;
import io.github.dunwu.javacore.container.bean.Student;

import java.util.Iterator;

public class TestMore {

    public static void main(String[] args) {
        Course c1 = new Course("英语", 3);    // 第一门课程
        Course c2 = new Course("计算机", 5);    // 第二门课程
        Student s1 = new Student("张三", 20);
        Student s2 = new Student("李四", 21);
        Student s3 = new Student("王五", 22);
        Student s4 = new Student("赵六", 23);
        Student s5 = new Student("孙七", 24);
        Student s6 = new Student("钱八", 24);
        // 第一门课程有三个学生参加
        c1.getAllStudents().add(s1);
        c1.getAllStudents().add(s2);
        c1.getAllStudents().add(s6);
        s1.getAllCourses().add(c1);
        s2.getAllCourses().add(c1);
        s6.getAllCourses().add(c1);
        // 第二门课程有六个学生参加
        c2.getAllStudents().add(s1);
        c2.getAllStudents().add(s2);
        c2.getAllStudents().add(s3);
        c2.getAllStudents().add(s4);
        c2.getAllStudents().add(s5);
        c2.getAllStudents().add(s6);
        s1.getAllCourses().add(c2);
        s2.getAllCourses().add(c2);
        s3.getAllCourses().add(c2);
        s4.getAllCourses().add(c2);
        s5.getAllCourses().add(c2);
        s6.getAllCourses().add(c2);
        // 输出一门课程的信息，观察一门课程有多少个学生参加\
        System.out.println(c1);
        Iterator<Student> iter1 = c1.getAllStudents().iterator();
        while (iter1.hasNext()) {
            Student s = iter1.next();
            System.out.println("\t|- " + s);
        }
        // 通过学生找到学生参加的课程
        System.out.println(s6);
        Iterator<Course> iter2 = s6.getAllCourses().iterator();
        while (iter2.hasNext()) {
            Course c = iter2.next();
            System.out.println("\t|- " + c);
        }
    }

}
