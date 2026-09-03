package io.github.dunwu.javacore.datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例：String 的不可变性（immutability）。
 * <p>
 * String 的不可变性由三层设计共同保证：
 * <ol>
 *     <li>类被 {@code final} 修饰，<b>不允许被继承</b>——否则子类可以覆写方法偷偷改动内容</li>
 *     <li>存放字符的数组字段被 {@code private final} 修饰，引用一旦赋值就不可再指向别处
 *     （JDK 9 起是 {@code private final byte[] value} 加一个 {@code coder} 标记编码，
 *     JDK 8 及之前是 {@code private final char[] value}，见 JEP 254 Compact Strings）</li>
 *     <li>类<b>不提供任何修改该数组的公开方法</b>。所有「看起来在修改字符串」的方法
 *     （{@code substring}、{@code concat}、{@code replace}、{@code toUpperCase}、{@code trim} 等）
 *     都只是<b>计算出一个新串并返回</b>，原串分毫未动</li>
 * </ol>
 * 需要注意 {@code final byte[] value} 只是让<b>引用</b>不可变，数组元素本身仍可写。
 * JDK 是通过「不暴露该数组 + 构造时做防御性拷贝」来堵死这条路子的，见 {@link #defensiveCopy()}。
 * <p>
 * 不可变换来三个直接好处：
 * <ul>
 *     <li><b>线程安全</b>——内容永不变，多个线程共享同一个实例无需任何同步</li>
 *     <li><b>hashCode 可以只算一次并缓存</b>——因此 String 是 HashMap key 的理想选择；
 *     反过来，可变对象作 key 会在内容被改动后「查不到自己」</li>
 *     <li><b>字符串常量池可以安全共享</b>——若 String 可变，共享同一个实例就会让一处改动波及全部引用者</li>
 * </ul>
 * <p>
 * <b>一段历史</b>：JDK 7u6 之前 {@code substring} 并不复制字符，而是与原串<b>共享同一个 value 数组</b>，
 * 只额外记录 offset 与 count。这让「从一个大字符串上截一小段」会连带持有整个大数组而无法回收，
 * 是当年常见的内存泄漏源。JDK 7u6 起改为 {@code Arrays.copyOfRange} 真拷贝，代价是截取变慢，
 * 换来的是不再有泄漏风险。
 * <p>
 * 相关示例：{@link StringBuilderDemo}（拼接性能与 String / StringBuilder / StringBuffer 三者选型）、
 * {@code String判等}（{@code ==} 与 {@code equals} 的差异、{@code intern()}）、
 * {@code String拼接}（常量折叠的字节码证据）。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class StringImmutabilityDemo {

    /**
     * ① 所有「看起来在修改」的方法都只是返回新对象，原串始终不变
     */
    public static void operationsReturnNewObject() {
        String origin = "Java";
        String upper = origin.toUpperCase();
        String replaced = origin.replace('a', 'o');
        String sub = origin.substring(0, 2);
        String concatenated = origin.concat("Core");

        System.out.println("原串: " + origin);
        System.out.println("toUpperCase() 返回: " + upper + "，此时原串: " + origin);
        System.out.println("replace('a', 'o') 返回: " + replaced + "，此时原串: " + origin);
        System.out.println("substring(0, 2) 返回: " + sub + "，此时原串: " + origin);
        System.out.println("concat(\"Core\") 返回: " + concatenated + "，此时原串: " + origin);
        // 四个返回值的内容都与原串不同，因此必然是四个新建的对象
        System.out.println("四个结果都不是原对象: "
            + (upper != origin && replaced != origin && sub != origin && concatenated != origin));
    }

    /**
     * ② {@code +=} 也不是「修改」，而是让引用改指向一个新对象
     */
    public static void reassignNotMutate() {
        String str = "a";
        // before 与 str 此刻指向同一个对象
        String before = str;
        // 这一行编译后等价于 str = new StringBuilder(str).append("b").toString()，
        // 结果是新建一个 "ab" 对象，再让 str 指过去；原来的 "a" 对象没有被改动
        str += "b";

        System.out.println("拼接前 str: " + before);
        System.out.println("拼接后 str: " + str);
        System.out.println("原来那个对象的内容仍是: " + before);
        System.out.println("拼接后 str 与原对象是同一个: " + (str == before));
    }

    /**
     * ③ 构造时与取出时都会做防御性拷贝，因此外部改数组影响不到 String
     */
    public static void defensiveCopy() {
        char[] chars = { 'J', 'a', 'v', 'a' };
        String str = new String(chars);
        // 改动源数组：如果 new String(char[]) 只是保存了数组引用，这里就会把 str 一起改掉
        chars[0] = 'X';
        System.out.println("用 char[] 构造出的 String: " + str);
        System.out.println("把数组首元素改成 X 后，String 仍是: " + str);

        // 反方向同理：toCharArray() 返回的是副本，改它也不会影响 String
        char[] extracted = str.toCharArray();
        extracted[0] = 'Y';
        System.out.println("把 toCharArray() 结果首元素改成 Y 后，String 仍是: " + str);
    }

    /**
     * ④ 编译期常量折叠之所以安全，前提正是 String 不可变
     */
    public static void compileTimeFolding() {
        // 两个纯字面量相加属于「编译期常量表达式」，javac 会直接折叠成 "ab" 放进常量池，
        // 运行时不存在任何拼接动作。这个优化敢做，是因为 String 不可变——
        // 假如 String 可变，折叠就会让原本彼此独立的对象被意外共享，改动一个会波及另一个。
        String folded = "a" + "b";
        String literal = "ab";
        System.out.println("纯字面量拼接后与 \"ab\" 是同一个对象: " + (folded == literal));

        // 一旦有变量参与，表达式就不再是编译期常量，javac 不会折叠，
        // 于是运行期新建对象，与常量池里的 "ab" 不是同一个。
        // 注意：这里的 part 若声明为 final，就会被当成编译期常量而重新折叠，结论随之改变。
        String part = "a";
        String runtime = part + "b";
        System.out.println("含变量的拼接与 \"ab\" 是同一个对象: " + (runtime == literal));
        System.out.println("但两者内容相等: " + runtime.equals(literal));
    }

    /**
     * ⑤ 不可变的好处：线程安全、hashCode 可缓存、常量池可共享；并用「可变对象作 key」做反例
     */
    public static void benefits() {
        System.out.println("好处一：内容永不变，因此多线程共享同一个 String 无需任何同步");

        String key = "id";
        Map<String, Integer> safeMap = new HashMap<>();
        safeMap.put(key, 1);
        System.out.println("好处二：hashCode 只需算一次即可缓存，所以 String 适合做 key，取值得到: "
            + safeMap.get("id"));

        // 反例：可变对象作 key。List 重写了 hashCode（由元素内容决定），
        // 一旦 put 之后改动了它的内容，hashCode 随之改变，再用「与当初等值的 key」去查就落到了别的桶上。
        List<Integer> mutableKey = new ArrayList<>(Arrays.asList(1, 2));
        List<Integer> equalToOriginal = Arrays.asList(1, 2);
        Map<List<Integer>, String> unsafeMap = new HashMap<>();
        unsafeMap.put(mutableKey, "value");
        System.out.println("改动内容前，用等值的 key 能取到: " + unsafeMap.get(equalToOriginal));
        mutableKey.add(3);
        System.out.println("改动内容后，用等值的 key 能取到: " + unsafeMap.get(equalToOriginal));

        // 另一个反例：StringBuilder 没有重写 equals 与 hashCode，
        // 内容相同的两个实例在 HashMap 眼里毫无关系，因此它根本不能当 key 用。
        System.out.println("内容相同的两个 StringBuilder 是否 equals: "
            + new StringBuilder("id").equals(new StringBuilder("id")));
    }

    /**
     * ⑥ 常量池：正因为不可变，同一个字面量才能被安全地共享
     * <p>
     * 判等的完整规则与 {@code intern()} 的更多用法见 {@code String判等}，这里只强调它与不可变性的因果关系。
     */
    public static void constantPool() {
        String literal = "shared";
        String anotherLiteral = "shared";
        String byNew = new String("shared");

        System.out.println("两个字面量是同一个对象: " + (literal == anotherLiteral));
        System.out.println("new String(\"shared\") 与字面量是同一个对象: " + (literal == byNew));
        System.out.println("但两者内容相等: " + literal.equals(byNew));
        System.out.println("intern() 之后与字面量是同一个对象: " + (literal == byNew.intern()));
    }

    /**
     * 依次演示不可变性的六个侧面
     */
    public static void demo() {
        operationsReturnNewObject();
        reassignNotMutate();
        defensiveCopy();
        compileTimeFolding();
        benefits();
        constantPool();
    }

    public static void main(String[] args) {
        demo();
    }

}

// Output:
// 原串: Java
// toUpperCase() 返回: JAVA，此时原串: Java
// replace('a', 'o') 返回: Jovo，此时原串: Java
// substring(0, 2) 返回: Ja，此时原串: Java
// concat("Core") 返回: JavaCore，此时原串: Java
// 四个结果都不是原对象: true
// 拼接前 str: a
// 拼接后 str: ab
// 原来那个对象的内容仍是: a
// 拼接后 str 与原对象是同一个: false
// 用 char[] 构造出的 String: Java
// 把数组首元素改成 X 后，String 仍是: Java
// 把 toCharArray() 结果首元素改成 Y 后，String 仍是: Java
// 纯字面量拼接后与 "ab" 是同一个对象: true
// 含变量的拼接与 "ab" 是同一个对象: false
// 但两者内容相等: true
// 好处一：内容永不变，因此多线程共享同一个 String 无需任何同步
// 好处二：hashCode 只需算一次即可缓存，所以 String 适合做 key，取值得到: 1
// 改动内容前，用等值的 key 能取到: value
// 改动内容后，用等值的 key 能取到: null
// 内容相同的两个 StringBuilder 是否 equals: false
// 两个字面量是同一个对象: true
// new String("shared") 与字面量是同一个对象: false
// 但两者内容相等: true
// intern() 之后与字面量是同一个对象: true
