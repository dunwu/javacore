package io.github.dunwu.javacore.datatype;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 演示 equals 与 compareTo 不一致引发的问题：{@code indexOf} 用 equals，而 {@code binarySearch} 用 compareTo。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
public class EqualsCompareToDemo {

    /**
     * 演示 equals 与 compareTo 不一致的问题：indexOf 用 equals，binarySearch 用 compareTo
     */
    public static void demo() {
        wrong();
        right();
    }

    public static void main(String[] args) {
        demo();
    }

    public static void wrong() {

        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "zhang"));
        list.add(new Student(2, "wang"));
        Student student = new Student(2, "li");

        System.out.println("ArrayList.indexOf");
        int index1 = list.indexOf(student);
        Collections.sort(list);
        System.out.println("Collections.binarySearch");
        int index2 = Collections.binarySearch(list, student);

        System.out.println("index1 = " + index1);
        System.out.println("index2 = " + index2);
    }

    public static void right() {

        List<StudentRight> list = new ArrayList<>();
        list.add(new StudentRight(1, "zhang"));
        list.add(new StudentRight(2, "wang"));
        StudentRight student = new StudentRight(2, "li");

        System.out.println("ArrayList.indexOf");
        int index1 = list.indexOf(student);
        Collections.sort(list);
        System.out.println("Collections.binarySearch");
        int index2 = Collections.binarySearch(list, student);

        System.out.println("index1 = " + index1);
        System.out.println("index2 = " + index2);
    }

    @Data
    @AllArgsConstructor
    public static class Student implements Comparable<Student> {

        private int id;
        private String name;

        @Override
        public int compareTo(Student other) {
            int result = Integer.compare(other.id, id);
            if (result == 0) { System.out.println("this " + this + " == other " + other); }
            return result;
        }

    }

    @Data
    @AllArgsConstructor
    public static class StudentRight implements Comparable<StudentRight> {

        private int id;
        private String name;

        @Override
        public int compareTo(StudentRight other) {
            return Comparator.comparing(StudentRight::getName)
                .thenComparingInt(StudentRight::getId)
                .compare(this, other);
        }

    }

}
// Output:
// ArrayList.indexOf
// Collections.binarySearch
// this EqualsCompareToDemo.Student(id=2, name=wang) == other EqualsCompareToDemo.Student(id=2, name=li)
// index1 = -1
// index2 = 0
// ArrayList.indexOf
// Collections.binarySearch
// index1 = -1
// index2 = -1
