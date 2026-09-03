package io.github.dunwu.javacore.datatype;

import java.util.HashMap;
import java.util.Map;

/**
 * 包装类的四类常见陷阱
 * <p>
 * {@link Integer判等} 与 {@link 包装类装箱拆箱} 讲的是装箱拆箱的基本写法和 Integer 缓存对 {@code ==} 的影响，
 * 本类补齐其余在生产中真正会出事的边界场景：
 * <ul>
 *     <li>{@link #cacheRange()} —— 各包装类的缓存范围并不相同，Float/Double 甚至完全不缓存</li>
 *     <li>{@link #unboxingNpe()} —— 包装类为 null 时拆箱会抛 NullPointerException</li>
 *     <li>{@link #ternaryNpe()} —— 三目运算符混合包装类与基本类型时的拆箱陷阱与类型提升</li>
 *     <li>{@link #loopBoxingPerformance()} —— 循环中用包装类型做累加器会产生大量临时对象</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class WrapperPitfallDemo {

    /**
     * 演示用的累加次数
     */
    private static final int LOOP_COUNT = 100_000;

    /**
     * 各包装类的缓存范围差异
     * <p>
     * 下面所有 {@code ==} 比较的都是<b>对象引用</b>（这正是本示例要演示的点），比较数值请一律用 {@code equals}。
     * 缓存由 {@code valueOf} 实现（享元模式），命中缓存时返回同一实例，{@code ==} 才是 true：
     * <ul>
     *     <li>Byte / Short / Integer / Long —— 缓存 {@code [-128, 127]}；其中 Integer 的上界可用
     *     {@code -XX:AutoBoxCacheMax=<size>} 调大，另外三个是常量，无法调整</li>
     *     <li>Character —— 缓存 {@code [0, 127]}，注意下界是 0 而不是 -128（char 本身无负值）</li>
     *     <li>Boolean —— 只有 {@code TRUE} / {@code FALSE} 两个实例，永远命中</li>
     *     <li>Float / Double —— <b>完全不缓存</b>，因为浮点取值几乎不会重复，缓存没有收益</li>
     * </ul>
     */
    public static void cacheRange() {
        // Byte 的取值范围恰好就是缓存范围，因此不存在「越界」的情况
        System.out.println("Byte      127 == 127 : " + (Byte.valueOf((byte) 127) == Byte.valueOf((byte) 127))
            + "（Byte 全部取值都在缓存内）");
        System.out.println("Short     127 == 127 : " + (Short.valueOf((short) 127) == Short.valueOf((short) 127))
            + " | 128 == 128 : " + (Short.valueOf((short) 128) == Short.valueOf((short) 128)));
        System.out.println("Integer   127 == 127 : " + (Integer.valueOf(127) == Integer.valueOf(127))
            + " | 128 == 128 : " + (Integer.valueOf(128) == Integer.valueOf(128)));
        System.out.println("Long      127 == 127 : " + (Long.valueOf(127L) == Long.valueOf(127L))
            + " | 128 == 128 : " + (Long.valueOf(128L) == Long.valueOf(128L)));
        System.out.println("Character 127 == 127 : " + (Character.valueOf((char) 127) == Character.valueOf((char) 127))
            + " | 128 == 128 : " + (Character.valueOf((char) 128) == Character.valueOf((char) 128)));
        System.out.println("Boolean   true == true : " + (Boolean.valueOf(true) == Boolean.valueOf(true)));
        System.out.println("Float     1.0 == 1.0 : " + (Float.valueOf(1.0f) == Float.valueOf(1.0f)));
        System.out.println("Double    1.0 == 1.0 : " + (Double.valueOf(1.0) == Double.valueOf(1.0)));
    }

    /**
     * 拆箱 NPE：包装类为 null 时，一旦需要基本类型值就会抛 NullPointerException
     * <p>
     * 编译器会自动插入 {@code intValue()} 之类的调用，因此 NPE 抛出点在字节码层面是「对 null 调用方法」，
     * 但源代码上往往看不到任何方法调用，堆栈信息很容易让人困惑。
     */
    public static void unboxingNpe() {
        Integer nullInteger = null;

        // 场景一：null 包装类赋给基本类型变量
        try {
            int value = nullInteger;
            System.out.println("不会执行到这里: " + value);
        } catch (NullPointerException e) {
            System.out.println("场景一 int value = nullInteger 抛出 NullPointerException");
        }

        // 场景二：null 包装类参与算术运算，运算前必须先拆箱
        try {
            int sum = nullInteger + 1;
            System.out.println("不会执行到这里: " + sum);
        } catch (NullPointerException e) {
            System.out.println("场景二 nullInteger + 1 抛出 NullPointerException");
        }

        // 场景三：最常见的真实来源 —— Map.get 未命中返回 null，而接收方是基本类型
        Map<String, Integer> scores = new HashMap<>();
        try {
            int score = scores.get("math");
            System.out.println("不会执行到这里: " + score);
        } catch (NullPointerException e) {
            System.out.println("场景三 scores.get(\"math\") 返回 null，拆箱时抛出 NullPointerException");
        }
    }

    /**
     * 三目运算符的拆箱陷阱
     * <p>
     * 按 JLS 15.25，当第二、第三个操作数<b>一个是包装类型、一个是基本类型</b>时，条件表达式的类型是基本类型，
     * 被选中的分支会被拆箱。若该分支恰好是 null，就抛 NPE。危险之处在于：条件本身可能与「是否为 null」毫无关系，
     * 开发者不会意识到这里藏着拆箱。
     */
    public static void ternaryNpe() {
        // 场景一：条件是「是否使用缓存」，与 fromCache 是否为 null 无关
        boolean useCache = true;
        Integer fromCache = null; // 缓存未命中
        int fallback = -1;
        try {
            int value = useCache ? fromCache : fallback;
            System.out.println("不会执行到这里: " + value);
        } catch (NullPointerException e) {
            System.out.println("场景一 三目运算符选中 null 分支时拆箱抛出 NullPointerException");
        }

        // 场景二：两个分支是不同的包装类型时，会做二进制数值提升，结果类型出乎意料
        Integer intValue = 1;
        Long longValue = 2L;
        Object promoted = true ? intValue : longValue;
        System.out.println("场景二 true ? Integer : Long 的结果类型是 " + promoted.getClass().getSimpleName()
            + "，值为 " + promoted);

        // 场景三：安全写法 —— 先显式判空，让两个分支都是包装类型，避免隐式拆箱
        Integer safe = (fromCache == null) ? Integer.valueOf(fallback) : fromCache;
        System.out.println("场景三 显式判空后取值: " + safe);
    }

    /**
     * 反例：累加器声明为包装类型 Long
     * <p>
     * 每次 {@code sum += i} 都要经过「拆箱 → 相加 → 重新装箱」三步，循环体会产生 count 个临时 Long 对象
     * （累加值很快超出 [-128, 127] 缓存范围，无法复用实例），既增加耗时也加重 GC 负担。
     */
    public static long sumByBoxing(int count) {
        Long sum = 0L;
        for (long i = 0; i < count; i++) {
            sum += i;
        }
        return sum;
    }

    /**
     * 正例：累加器声明为基本类型 long，全程不产生任何对象
     */
    public static long sumByPrimitive(int count) {
        long sum = 0L;
        for (long i = 0; i < count; i++) {
            sum += i;
        }
        return sum;
    }

    /**
     * 对比循环中使用包装类型与基本类型做累加器的性能差异
     * <p>
     * 这就是《Effective Java》item 5「避免创建不必要的对象」举的例子。
     * 耗时受 JIT 预热与机器负载影响，属于非确定性输出，关注结论即可：两种写法结果必须一致，而装箱写法明显更慢。
     */
    public static void loopBoxingPerformance() {
        long startBoxing = System.nanoTime();
        long boxingSum = sumByBoxing(LOOP_COUNT);
        long boxingCost = System.nanoTime() - startBoxing;

        long startPrimitive = System.nanoTime();
        long primitiveSum = sumByPrimitive(LOOP_COUNT);
        long primitiveCost = System.nanoTime() - startPrimitive;

        System.out.println("累加 0.." + (LOOP_COUNT - 1) + " 的结果: " + primitiveSum);
        System.out.println("两种写法结果一致: " + (boxingSum == primitiveSum));
        System.out.println("包装类型 Long 累加耗时: " + (boxingCost / 1000) + " 微秒");
        System.out.println("基本类型 long 累加耗时: " + (primitiveCost / 1000) + " 微秒");
    }

    /**
     * 依次演示缓存范围差异、拆箱 NPE、三目运算符陷阱与循环装箱性能
     */
    public static void demo() {
        cacheRange();
        unboxingNpe();
        ternaryNpe();
        loopBoxingPerformance();
    }

    public static void main(String[] args) {
        demo();
    }
    // Output:
    // Byte      127 == 127 : true（Byte 全部取值都在缓存内）
    // Short     127 == 127 : true | 128 == 128 : false
    // Integer   127 == 127 : true | 128 == 128 : false
    // Long      127 == 127 : true | 128 == 128 : false
    // Character 127 == 127 : true | 128 == 128 : false
    // Boolean   true == true : true
    // Float     1.0 == 1.0 : false
    // Double    1.0 == 1.0 : false
    // 场景一 int value = nullInteger 抛出 NullPointerException
    // 场景二 nullInteger + 1 抛出 NullPointerException
    // 场景三 scores.get("math") 返回 null，拆箱时抛出 NullPointerException
    // 场景一 三目运算符选中 null 分支时拆箱抛出 NullPointerException
    // 场景二 true ? Integer : Long 的结果类型是 Long，值为 1
    // 场景三 显式判空后取值: -1
    // 累加 0..99999 的结果: 4999950000
    // 两种写法结果一致: true
    // 包装类型 Long 累加耗时: （因机器而异）微秒
    // 基本类型 long 累加耗时: （因机器而异）微秒
}
