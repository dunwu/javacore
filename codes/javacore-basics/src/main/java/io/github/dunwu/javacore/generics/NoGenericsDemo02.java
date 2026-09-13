package io.github.dunwu.javacore.generics;

/**
 * 不使用泛型的隐患示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see GenericsClassDemo01
 * @since 2019-03-20
 */
public class NoGenericsDemo02 {

    /**
     * 对比示例：不使用泛型时用 Object 存取 + 强转，需要程序员自己保证类型安全。
     */
    public static void demo() {
        Info info = new Info();
        info.setValue("abc");
        String str = (String) info.getValue();
        System.out.println("str = [" + str + "]");
    }

    public static void main(String[] args) {
        demo();
    }

    static class Info {

        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }

}
// Output: str = [abc]
//
// 与 NoGenericsDemo 不同：这里存进去的本来就是 String，取出后强转不会失败，因此没有任何异常输出。
// 本示例说明的是：不用泛型时类型安全完全依赖程序员自己保证，编译器无从检查。
// （JDK 21 实测：stdout 一行，stderr 为空，退出码 0）
