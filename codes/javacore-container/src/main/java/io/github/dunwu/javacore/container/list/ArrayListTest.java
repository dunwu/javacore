package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ArrayListTest {

    public static void main(String[] args) {
        // fill the staff array list with three Employee objects
        List<Employee> staff = new ArrayList<>();

        staff.add(new Employee("Carl Cracker", 75000, 1987, 12, 15));
        staff.add(new Employee("Harry Hacker", 50000, 1989, 10, 1));
        staff.add(new Employee("Tony Tester", 40000, 1990, 3, 15));

        // raise everyone's salary by 5%
        for (Employee e : staff) {
            e.raiseSalary(5);
        }

        // print out information about all Employee objects
        for (Employee e : staff) {
            System.out.println("name=" + e.getName() + ",salary=" + e.getSalary() + ",hireDay="
                + e.getHireDay());
        }
    }

    static class Employee {

        private String name;

        private double salary;

        private Date hireDay;

        public Employee(String n, double s, int year, int month, int day) {
            name = n;
            salary = s;
            GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
            hireDay = calendar.getTime();
        }

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }

        public Date getHireDay() {
            return hireDay;
        }

        public void raiseSalary(double byPercent) {
            double raise = salary * byPercent / 100;
            salary += raise;
        }

    }

}
