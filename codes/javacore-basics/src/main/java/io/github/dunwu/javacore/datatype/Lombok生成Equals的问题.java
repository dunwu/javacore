package io.github.dunwu.javacore.datatype;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
@Slf4j
public class Lombok生成Equals的问题 {

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        Person person1 = new Person("zhuye", "001");
        Person person2 = new Person("Joseph", "001");
        log.info("person1.equals(person2) ? {}", person1.equals(person2));
    }

    public static void test2() {
        Employee employee1 = new Employee("zhuye", "001", "bkjk.com");
        Employee employee2 = new Employee("Joseph", "002", "bkjk.com");
        log.info("employee1.equals(employee2) ? {}", employee1.equals(employee2));
    }

    @Data
    static class Person {

        @EqualsAndHashCode.Exclude
        private String name;
        private String identity;

        public Person(String name, String identity) {
            this.name = name;
            this.identity = identity;
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class Employee extends Person {

        private String company;

        public Employee(String name, String identity, String company) {
            super(name, identity);
            this.company = company;
        }

    }

}
