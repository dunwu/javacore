package io.github.dunwu.javacore.jdk16.record;

/**
 * Java 16 Record 进阶示例。
 * <p>
 * 除了自动生成的成员外，record 还支持：
 * <ul>
 * <li>紧凑构造器（compact constructor）：在字段赋值前做参数校验或规范化，无需写参数列表</li>
 * <li>自定义实例方法、静态方法和静态字段</li>
 * <li>实现接口（但不能继承类，也不能被继承）</li>
 * <li>局部 record：定义在方法内部，适合临时数据聚合</li>
 * </ul>
 */
public class RecordAdvancedDemo {

    /**
     * 示例 1：紧凑构造器校验与自定义实例方法、静态工厂方法
     */
    public static void compactConstructorAndMethods() {
        // 紧凑构造器中的校验逻辑生效
        try {
            new Range(10, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("校验失败: " + e.getMessage());
        }

        Range range = new Range(1, 10);
        // 自定义实例方法
        System.out.println("range 长度: " + range.length());
        // 静态工厂方法
        System.out.println("静态工厂: " + Range.of(5));
    }

    /**
     * 示例 2：record 实现接口
     */
    public static void recordImplementsInterface() {
        Printable printable = new Message("Java 16");
        printable.print();
    }

    /**
     * 示例 3：局部 record——方法内定义，用于临时数据聚合
     */
    public static void localRecord() {
        record NameCount(String name, int count) {

        }
        NameCount nc = new NameCount("record", 3);
        System.out.println("局部 record: " + nc);
    }

    public static void main(String[] args) {
        compactConstructorAndMethods();
        recordImplementsInterface();
        localRecord();
    }

    /**
     * 紧凑构造器：省略参数列表，直接对组件参数做校验/规范化，编译器自动补全字段赋值。
     */
    record Range(int start, int end) {

        Range {
            if (start > end) {
                throw new IllegalArgumentException("start 不能大于 end");
            }
        }

        /**
         * 自定义实例方法
         */
        int length() {
            return end - start + 1;
        }

        /**
         * 静态工厂方法
         */
        static Range of(int n) {
            return new Range(0, n);
        }

    }

    interface Printable {

        void print();

    }

    /**
     * record 可以实现接口，并重写访问器方法（例如做防御性处理）
     */
    record Message(String content) implements Printable {

        @Override
        public void print() {
            System.out.println("Message: " + content);
        }

    }

}
// Output:
// 校验失败: start 不能大于 end
// range 长度: 10
// 静态工厂: Range[start=0, end=5]
// Message: Java 16
// 局部 record: NameCount[name=record, count=3]
