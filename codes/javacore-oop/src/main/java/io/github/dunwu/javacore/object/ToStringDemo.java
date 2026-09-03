package io.github.dunwu.javacore.object;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例：重写 {@code toString}。
 * <p>
 * {@code Object.toString} 的默认实现返回「类的全限定名@十六进制hashCode」，写进日志后完全看不出对象内容，
 * 排查问题时只能靠猜。《Effective Java》item 12 的建议是：<b>所有值类都应当重写 toString</b>，
 * 让它返回对象中所有值得关注的字段。
 * <p>
 * <b>两个实践要点</b>：
 * <ul>
 *     <li>集合与嵌套对象的 toString 会<b>递归</b>调用元素的 toString。只要有一层没重写，
 *     打印出来就是一串地址，见 {@link #nestedAndCollection()}</li>
 *     <li>重写后要想清楚「是否承诺格式稳定」。一旦有代码开始解析 toString 的返回值，
 *     这个格式就变成了对外契约，改动即破坏兼容。建议在 Javadoc 中明确写出格式，或明确声明不保证</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see EqualsAndHashCodeDemo
 */
public class ToStringDemo {

    /**
     * 不重写 toString 时的默认输出
     */
    public static void defaultToString() {
        Plain plain = new Plain("张三", 30);
        String defaultText = plain.toString();
        System.out.println("默认 toString 的格式是「类名@十六进制hashCode」: "
            + (defaultText.startsWith(Plain.class.getName() + "@")
                && defaultText.endsWith("@" + Integer.toHexString(plain.hashCode()))));
        // 字符串拼接会隐式调用 toString，日志里打印出来的就是这串没有信息量的内容
        System.out.println("隐式调用: " + plain);
    }

    /**
     * 重写后：日志能自解释
     */
    public static void overriddenToString() {
        Employee employee = new Employee("张三", 30);
        System.out.println("重写后的 toString: " + employee);
        // 好的 toString 应当包含所有值得关注的字段，而不是只给个类名
        System.out.println("包含全部字段: "
            + (employee.toString().contains("name='张三'") && employee.toString().contains("age=30")));
    }

    /**
     * 集合与嵌套对象会递归调用元素的 toString
     */
    public static void nestedAndCollection() {
        List<Employee> employees = Arrays.asList(new Employee("张三", 30), new Employee("李四", 25));
        System.out.println("List: " + employees);

        Department department = new Department("研发部", employees);
        System.out.println("嵌套对象: " + department);

        // Map 的 toString 会同时调用 key 与 value 的 toString
        Map<String, Employee> map = new LinkedHashMap<>();
        map.put("emp001", new Employee("张三", 30));
        System.out.println("Map: " + map);
    }

    /**
     * 依次演示默认输出、重写后的输出以及集合与嵌套对象的递归调用
     */
    public static void demo() {
        defaultToString();
        overriddenToString();
        nestedAndCollection();
    }

    public static void main(String[] args) {
        demo();
    }

    /**
     * 不重写 toString 的普通类
     */
    static class Plain {

        private final String name;

        private final int age;

        Plain(String name, int age) {
            this.name = name;
            this.age = age;
        }

        String getName() {
            return name;
        }

        int getAge() {
            return age;
        }

    }

    /**
     * 重写了 toString 的值类。
     * <p>
     * 格式约定为 {@code Employee{name='xxx', age=n}}，本示例的测试依赖这个格式。
     */
    static class Employee {

        private final String name;

        private final int age;

        Employee(String name, int age) {
            this.name = name;
            this.age = age;
        }

        String getName() {
            return name;
        }

        int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Employee{name='" + name + "', age=" + age + "}";
        }

    }

    /**
     * 持有 Employee 列表的嵌套对象，用于演示 toString 的递归调用
     */
    static class Department {

        private final String name;

        private final List<Employee> employees;

        Department(String name, List<Employee> employees) {
            this.name = name;
            this.employees = employees;
        }

        @Override
        public String toString() {
            return "Department{name='" + name + "', employees=" + employees + "}";
        }

    }

}
