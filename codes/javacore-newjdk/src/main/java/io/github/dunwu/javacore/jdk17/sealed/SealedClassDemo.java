package io.github.dunwu.javacore.jdk17.sealed;

/**
 * Java 17 密封类（Sealed Classes）基础示例。
 * <p>
 * 密封类在 Java 15/16 预览、Java 17 正式转正（JEP 409）。
 * 它用于精确控制类型继承体系：一个 sealed 类/接口可以通过 {@code permits}
 * 显式声明哪些类允许继承/实现它。
 * <p>
 * 直接子类必须三选一声明自己的继承策略：
 * <ul>
 * <li>{@code final}：不允许再被继承</li>
 * <li>{@code sealed}：继续受限继承，需再次 permits</li>
 * <li>{@code non-sealed}：放开限制，任何类都可以继承它</li>
 * </ul>
 * 密封类配合 instanceof 模式匹配 / switch 模式匹配，可以实现类似代数数据类型的穷举检查。
 */
public class SealedClassDemo {

    /**
     * 示例 1：只有 permits 中声明的子类可以实例化，配合 instanceof 模式匹配做类型分派
     */
    public static void sealedHierarchy() {
        Employee fullTime = new FullTimeEmployee("张三", 20000);
        Employee partTime = new PartTimeEmployee("李四", 100);
        Employee intern = new Intern("王五");

        System.out.println(describe(fullTime));
        System.out.println(describe(partTime));
        System.out.println(describe(intern));
    }

    /**
     * 示例 2：编译期即可确定继承体系是封闭的，getPermittedSubclasses 可反射查看
     */
    public static void permittedSubclasses() {
        System.out.println("Employee 的许可子类: ");
        for (Class<?> sub : Employee.class.getPermittedSubclasses()) {
            System.out.println("  " + sub.getSimpleName());
        }
    }

    public static void main(String[] args) {
        sealedHierarchy();
        permittedSubclasses();
    }

    private static String describe(Employee employee) {
        if (employee instanceof FullTimeEmployee fte) {
            return fte.name() + " 是全职员工，月薪 " + fte.monthlySalary();
        } else if (employee instanceof PartTimeEmployee pte) {
            return pte.name() + " 是兼职员工，时薪 " + pte.hourlyRate();
        } else if (employee instanceof Intern intern) {
            return intern.name() + " 是实习生";
        }
        // if/else 链在 Java 17 中尚不支持穷举检查（switch 模式匹配在 Java 21 才正式支持），
        // 因此仍需保留兜底逻辑
        throw new IllegalStateException("不可达");
    }

    /**
     * 密封类：只允许 FullTimeEmployee、PartTimeEmployee、Intern 三个子类。
     * 子类与父类同文件时，permits 子句可省略（隐式许可本文件的子类）。
     */
    sealed interface Employee permits FullTimeEmployee, PartTimeEmployee, Intern {

        String name();

    }

    /**
     * final 子类：不允许再被继承
     */
    record FullTimeEmployee(String name, int monthlySalary) implements Employee {

    }

    /**
     * final 子类
     */
    record PartTimeEmployee(String name, int hourlyRate) implements Employee {

    }

    /**
     * sealed 子类：继续受限继承，只允许 GraduatedIntern。
     * 注意：内部类作为 sealed 子类必须是 static 的（不能隐式持有外部实例）
     */
    static sealed class Intern implements Employee permits GraduatedIntern {

        private final String name;

        Intern(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }

    }

    /**
     * non-sealed 子类：从这一代起放开限制，任何类都可以继承
     */
    static non-sealed class GraduatedIntern extends Intern {

        GraduatedIntern(String name) {
            super(name);
        }

    }

}
// Output:
// 张三 是全职员工，月薪 20000
// 李四 是兼职员工，时薪 100
// 王五 是实习生
// Employee 的许可子类:
//   FullTimeEmployee
//   PartTimeEmployee
//   Intern
