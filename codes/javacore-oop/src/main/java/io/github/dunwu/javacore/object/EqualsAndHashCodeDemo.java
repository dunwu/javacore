package io.github.dunwu.javacore.object;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 示例：重写 {@code equals} 与 {@code hashCode}。
 * <p>
 * 这两个方法必须<b>成对重写</b>，因为它们共同遵守一条约定：<b>equals 相等的两个对象，hashCode 必须相等</b>
 * （反过来不要求）。所有基于哈希的容器——{@code HashMap}、{@code HashSet}、{@code Hashtable}——
 * 都是「先用 hashCode 定位桶，再在桶内用 equals 逐个比对」。只重写其中一个，容器就会给出错误结果。
 * <p>
 * {@link #missingHashCode()} 是本示例最关键的一段：它演示了只重写 equals 会导致
 * 「明明 contains 判断相等，HashSet 里却存进了重复元素、HashMap 里也取不出值」。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ToStringDemo
 */
public class EqualsAndHashCodeDemo {

    /**
     * 不重写 equals 时的默认行为：{@code Object.equals} 就是 {@code ==}
     */
    public static void defaultEquals() {
        Plain p1 = new Plain("张三", 30);
        Plain p2 = new Plain("张三", 30);
        System.out.println("字段完全相同的两个对象 == : " + (p1 == p2));
        System.out.println("字段完全相同的两个对象 equals: " + p1.equals(p2));
        System.out.println("同一个引用 equals 自身: " + p1.equals(p1));
        System.out.println("与 null 比较: " + p1.equals(null));
    }

    /**
     * equals 的五条契约
     * <p>
     * 重写时最容易破坏的是<b>对称性</b>（子类加入新字段后与父类互相比较）和<b>传递性</b>，
     * 这也是《Effective Java》item 10 强调「优先用组合而不是继承来扩展值类」的原因。
     */
    public static void equalsContract() {
        Employee a = new Employee("张三", 30);
        Employee b = new Employee("张三", 30);
        Employee c = new Employee("李四", 25);

        // 自反性：x.equals(x) 必须为 true
        System.out.println("自反性 a.equals(a): " + a.equals(a));
        // 对称性：a.equals(b) 与 b.equals(a) 必须一致
        System.out.println("对称性 a.equals(b) == b.equals(a): " + (a.equals(b) == b.equals(a)));
        // 传递性：a=b 且 b=d，则 a=d
        Employee d = new Employee("张三", 30);
        System.out.println("传递性 a=b, b=d, 则 a.equals(d): " + (a.equals(b) && b.equals(d) && a.equals(d)));
        // 一致性：只要参与比较的字段没变，多次调用结果不变
        System.out.println("一致性 连续三次调用结果相同: " + (a.equals(b) && a.equals(b) && a.equals(b)));
        // 非空性：任何对象与 null 比较都必须返回 false，且不抛异常
        System.out.println("非空性 a.equals(null): " + a.equals(null));
        // 类型不同返回 false：这一步由 equals 里的 instanceof 判断负责
        System.out.println("与字符串比较 a.equals(\"张三\"): " + a.equals("张三"));
        System.out.println("字段不同 a.equals(c): " + a.equals(c));
    }

    /**
     * hashCode 的两条契约
     */
    public static void hashCodeContract() {
        Employee a = new Employee("张三", 30);
        Employee b = new Employee("张三", 30);

        // 契约一：equals 相等的两个对象，hashCode 必须相等（这是与 equals 的核心关联）
        System.out.println("equals 相等 => hashCode 相等: " + (a.equals(b) && a.hashCode() == b.hashCode()));
        // 契约二：同一对象在一次运行中多次调用 hashCode 必须返回相同值
        System.out.println("同一对象多次调用 hashCode 一致: " + (a.hashCode() == a.hashCode()));
        // 注意反过来不成立：hashCode 相等并不代表 equals 相等（哈希冲突）。
        // 因此 HashMap 定位到桶之后，还要在桶内用 equals 逐个比对，两步缺一不可。
        System.out.println("hashCode 是否参与 equals 判断: false（它只用于定位桶）");
    }

    /**
     * 反例：只重写 equals 而不重写 hashCode
     * <p>
     * 后果是哈希容器彻底失效——它先用 {@code Object.hashCode}（基于对象地址）把两个「相等」的对象
     * 放进了不同的桶，之后 {@code contains} / {@code get} 都找不到。这类 bug 在测试里很容易漏掉
     * （直接调 equals 是对的），一上线用 HashMap 缓存就出问题。
     */
    public static void missingHashCode() {
        OnlyEquals target = new OnlyEquals("张三", 30);

        Set<OnlyEquals> set = new HashSet<>();
        set.add(target);
        set.add(new OnlyEquals("张三", 30));
        System.out.println("HashSet 里的元素个数（应为 1）: " + set.size());
        System.out.println("contains 一个字段相同的新对象: " + set.contains(new OnlyEquals("张三", 30)));

        Map<OnlyEquals, String> map = new HashMap<>();
        map.put(target, "工程师");
        System.out.println("用原来的 key 能取到: " + map.get(target));
        System.out.println("用相等的新 key 能取到: " + map.get(new OnlyEquals("张三", 30)));
    }

    /**
     * 正例：equals 与 hashCode 一起重写后，哈希容器恢复正常
     */
    public static void correctHashCode() {
        Set<Employee> set = new HashSet<>();
        set.add(new Employee("张三", 30));
        set.add(new Employee("张三", 30));
        System.out.println("HashSet 里的元素个数: " + set.size());
        System.out.println("contains 一个字段相同的新对象: " + set.contains(new Employee("张三", 30)));

        Map<Employee, String> map = new HashMap<>();
        map.put(new Employee("张三", 30), "工程师");
        System.out.println("用相等的新 key 能取到: " + map.get(new Employee("张三", 30)));
    }

    /**
     * 依次演示默认行为、equals 契约、hashCode 契约，以及只重写 equals 的后果与正确写法
     */
    public static void demo() {
        defaultEquals();
        equalsContract();
        hashCodeContract();
        missingHashCode();
        correctHashCode();
    }

    public static void main(String[] args) {
        demo();
    }

    /**
     * 完全没有重写 equals / hashCode 的普通类，用于展示 Object 的默认行为
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
     * 只重写 equals，<b>故意不重写 hashCode</b>，用于演示破坏约定的后果
     */
    static class OnlyEquals {

        private final String name;

        private final int age;

        OnlyEquals(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof OnlyEquals)) {
                return false;
            }
            OnlyEquals other = (OnlyEquals) o;
            return age == other.age && Objects.equals(name, other.name);
        }

        // 这里刻意不写 hashCode：它继承 Object 的实现（与对象地址相关），
        // 于是两个 equals 为 true 的对象会拥有不同的 hashCode，违反约定

    }

    /**
     * 正确重写 equals 与 hashCode 的值类
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

        /**
         * 标准的 equals 写法，分三步：比引用 → 判类型 → 逐字段比较
         * <p>
         * JDK 16 起第 2、3 步可以用模式匹配简写为 {@code if (!(o instanceof Employee other)) return false;}
         */
        @Override
        public boolean equals(Object o) {
            // 1. 同一个引用直接返回 true，省掉后面的字段比较
            if (this == o) {
                return true;
            }
            // 2. 判空 + 判类型：instanceof 对 null 返回 false，因此这一步同时完成了判空
            if (!(o instanceof Employee)) {
                return false;
            }
            // 3. 逐字段比较：引用类型字段用 Objects.equals 避免 NPE，基本类型直接用 ==
            Employee other = (Employee) o;
            return age == other.age && Objects.equals(name, other.name);
        }

        /**
         * hashCode 必须使用与 equals <b>完全相同</b>的字段，否则约定被破坏
         */
        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

    }

}
