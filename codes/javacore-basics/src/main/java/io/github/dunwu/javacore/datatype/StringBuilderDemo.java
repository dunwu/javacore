package io.github.dunwu.javacore.datatype;

/**
 * StringBuilder 字符串拼接示例
 * <p>
 * String 是<b>不可变类</b>，每次用 {@code +} 拼接都会创建一个新的 String 对象，并把旧内容整体拷贝一遍。
 * 在循环中大量拼接时，拷贝代价随长度线性增长，总代价是 O(n^2)，同时产生大量待回收的临时对象。
 * <p>
 * StringBuilder 内部维护一个可变的 char 数组，{@code append} 只在数组尾部追加（容量不足时才扩容），
 * 因此循环拼接的总代价是 O(n)，是单线程下拼接字符串的首选。
 * <p>
 * 三者选型：
 * <ul>
 * <li>String —— 少量、固定的字符串操作</li>
 * <li>StringBuilder —— 单线程下的大量拼接（首选）</li>
 * <li>StringBuffer —— 多线程下的大量拼接（方法加了 synchronized，性能略低于 StringBuilder）</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
public class StringBuilderDemo {

    /**
     * 循环拼接次数。取值不宜过大，否则 String += 的 O(n^2) 拷贝会拖慢单元测试
     */
    private static final int LOOP_COUNT = 1000;

    /**
     * 依次演示 StringBuilder 常用 API 与两种拼接方式的对照
     */
    public static void demo() {
        commonApi();
        concatCompare();
    }

    /**
     * StringBuilder 常用 API：append / insert / delete / replace / reverse / setLength / capacity
     */
    public static void commonApi() {
        StringBuilder sb = new StringBuilder("Java");
        System.out.println("原始内容: " + sb);

        sb.append("Core"); // 尾部追加
        System.out.println("append(\"Core\") 后: " + sb);

        sb.insert(4, '-'); // 在索引 4 处插入
        System.out.println("insert(4, '-') 后: " + sb);

        sb.delete(4, 5); // 删除 [4, 5) 区间的字符
        System.out.println("delete(4, 5) 后: " + sb);

        sb.replace(0, 4, "JAVA"); // 把 [0, 4) 区间替换为新串
        System.out.println("replace(0, 4, \"JAVA\") 后: " + sb);

        sb.reverse(); // 反转
        System.out.println("reverse() 后: " + sb);

        sb.setLength(4); // 截断长度，超出部分丢弃
        System.out.println("setLength(4) 后: " + sb);

        // 无参构造的默认初始容量为 16，append 超过容量时按 2 倍 + 2 扩容
        System.out.println("new StringBuilder() 的初始容量: " + new StringBuilder().capacity());
    }

    /**
     * 对照 StringBuilder 与 String += 两种循环拼接方式
     * <p>
     * 关键点：两者拼出的<b>结果必须完全一致</b>，但耗时差距很大。
     * 如果结果不一致，说明某一方的拼接逻辑写错了——例如把循环变量 {@code i} 误写成字符串字面量 {@code "i"}。
     */
    public static void concatCompare() {
        String byBuilder = concatByStringBuilder(LOOP_COUNT);
        String byPlus = concatByPlus(LOOP_COUNT);

        System.out.println("两种写法拼接结果是否一致: " + byBuilder.equals(byPlus));
        System.out.println("拼接结果长度: " + byBuilder.length());
        System.out.println("拼接结果前 20 个字符: [" + byBuilder.substring(0, 20) + "]");
    }

    /**
     * 高效写法：用 StringBuilder 在循环中拼接
     *
     * @param count 拼接次数
     * @return 形如 {@code No.0\tNo.1\t...} 的字符串
     */
    public static String concatByStringBuilder(int count) {
        long start = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("No.").append(i).append('\t');
        }
        String result = sb.toString();
        System.out.println("StringBuilder 拼接 " + count + " 次耗时(ns): " + (System.nanoTime() - start));
        return result;
    }

    /**
     * 低效写法：用 String += 在循环中拼接（仅作对照，实际开发中应避免）
     *
     * @param count 拼接次数
     * @return 形如 {@code No.0\tNo.1\t...} 的字符串
     */
    public static String concatByPlus(int count) {
        long start = System.nanoTime();
        String str = "";
        for (int i = 0; i < count; i++) {
            str += "No." + i + '\t';
        }
        System.out.println("String += 拼接 " + count + " 次耗时(ns): " + (System.nanoTime() - start));
        return str;
    }

    public static void main(String[] args) {
        demo();
    }

}
