package io.github.dunwu.javacore.datatype;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 示例：String 常用 API。
 * <p>
 * 按用途分成八组：取值、字符与字节转换、查找与匹配、截取、修剪与比较、替换、拆分、拼接与格式化。
 * 其中 {@link #splitPitfall()}、{@link #replace()}、{@link #concatAndFormat()} 三节着重讲容易踩的坑。
 * <p>
 * 分工说明：
 * <ul>
 *     <li>不可变性、常量池、{@code ==} 与 {@code equals} 的差异 —— 见 {@link StringImmutabilityDemo} 与
 *     {@code String判等}</li>
 *     <li>拼接性能与 String / StringBuilder / StringBuffer 三者选型 —— 见 {@link StringBuilderDemo}</li>
 *     <li>{@code String.join}、{@code StringJoiner} —— 属 JDK 8 新增，见 javacore-newjdk 的
 *     {@code jdk8/util/StringJoinerDemo}</li>
 *     <li>{@code strip}、{@code isBlank}、{@code repeat}、{@code lines} —— 属 JDK 11 新增，见 javacore-newjdk 的
 *     {@code jdk11/string/StringEnhanceDemo}</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class StringDemo {

    /**
     * ① 取值：length / charAt / isEmpty
     */
    public static void lengthAndCharAt() {
        String word = "Computer";
        System.out.println("\"Computer\".length(): " + word.length());
        System.out.println("\"Computer\".charAt(4): " + word.charAt(4));
        // 空串与「只含空格的串」是两回事：后者 length 为 1，isEmpty 为 false
        System.out.println("\"\".isEmpty(): " + "".isEmpty());
        System.out.println("\" \".isEmpty(): " + " ".isEmpty());

        // 索引合法区间是 [0, length - 1]，取 length 就会越界
        try {
            word.charAt(word.length());
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("charAt(length) 抛出: " + e.getClass().getSimpleName());
        }
    }

    /**
     * ② 字符与字节转换：toCharArray / getChars / getBytes / valueOf
     */
    public static void charAndByteConversion() {
        System.out.println("toCharArray(): " + Arrays.toString("Baby".toCharArray()));

        // getChars(srcBegin, srcEnd, dst, dstBegin)：把 [srcBegin, srcEnd) 写入 dst，从 dstBegin 开始。
        // 这里先把目标数组填满 '_'，才能看清字符被写到了哪几个位置上
        char[] buffer = new char[10];
        Arrays.fill(buffer, '_');
        "Summer".getChars(0, 6, buffer, 2);
        System.out.println("getChars(0, 6, buffer, 2) 后: " + new String(buffer));

        System.out.println("String.valueOf(char[]): " + String.valueOf(new char[] { 'a', 'b' }));

        // getBytes 是乱码的主要来源：不传 Charset 时使用平台默认编码，换一台机器结果就变了。
        // 任何跨进程、跨网络的字节转换都应当显式指定字符集。
        byte[] utf8 = "中".getBytes(StandardCharsets.UTF_8);
        System.out.println("\"中\" 的 UTF-8 字节: " + Arrays.toString(utf8) + "，共 " + utf8.length + " 个");
        System.out.println("按 UTF-8 还原: " + new String(utf8, StandardCharsets.UTF_8));

        // ISO-8859-1 是单字节编码，装不下汉字，无法映射的字符会被替换成 '?'（字节值 63）
        byte[] latin1 = "中".getBytes(StandardCharsets.ISO_8859_1);
        System.out.println("\"中\" 的 ISO-8859-1 字节: " + Arrays.toString(latin1) + "，共 " + latin1.length + " 个");
    }

    /**
     * ③ 查找与匹配：indexOf / lastIndexOf / contains / startsWith / endsWith / matches
     */
    public static void search() {
        String origin = "How are you";
        System.out.println("indexOf(\"o\"): " + origin.indexOf("o"));
        System.out.println("indexOf(\"o\", 5): " + origin.indexOf("o", 5));
        System.out.println("lastIndexOf(\"o\"): " + origin.lastIndexOf("o"));
        System.out.println("lastIndexOf(\"o\", 5): " + origin.lastIndexOf("o", 5));
        // 没找到返回 -1 而不是抛异常，所以判空要显式比较 -1
        System.out.println("indexOf(\"z\"): " + origin.indexOf("z"));

        System.out.println("contains(\"are\"): " + origin.contains("are"));
        System.out.println("startsWith(\"How\"): " + origin.startsWith("How"));
        System.out.println("endsWith(\"you\"): " + origin.endsWith("you"));
        // startsWith 可以指定从哪个偏移量开始比对
        System.out.println("startsWith(\"are\", 4): " + origin.startsWith("are", 4));

        // matches 是「整串匹配」而不是「包含匹配」，这是最常见的误解
        String digits = "\\d+";
        System.out.println("\"12345\".matches(" + digits + "): " + "12345".matches(digits));
        System.out.println("\"a123\".matches(" + digits + "): " + "a123".matches(digits));
        System.out.println("想表达「串里有数字」要写成 \"a123\".matches(.*" + digits + "): "
            + "a123".matches(".*" + digits));
    }

    /**
     * ④ 截取：substring 的两个重载，区间一律左闭右开
     */
    public static void substring() {
        String str = "Hello World";
        System.out.println("substring(6): [" + str.substring(6) + "]");
        System.out.println("substring(0, 5): [" + str.substring(0, 5) + "]");
        System.out.println("左闭右开，substring(2, 5): [" + str.substring(2, 5) + "]");
        // begin 与 end 相等是合法的，得到空串
        System.out.println("substring(3, 3): [" + str.substring(3, 3) + "]");

        try {
            str.substring(6, str.length() + 10);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("endIndex 超出长度抛出: " + e.getClass().getSimpleName());
        }
    }

    /**
     * ⑤ 修剪与比较：trim / toLowerCase / toUpperCase / compareTo / equalsIgnoreCase
     */
    public static void trimCaseAndCompare() {
        System.out.println("trim(): [" + "    Night       ".trim() + "]");
        System.out.println("toLowerCase(): " + "China".toLowerCase());
        System.out.println("toUpperCase(): " + "China".toUpperCase());

        // trim 只去掉码点 <= U+0020 的字符，全角空格（U+3000）不在其列。
        // 需要按 Unicode 标准去空白，得用 JDK 11 的 strip，见 javacore-newjdk 的 StringEnhanceDemo
        String fullWidth = "\u3000Night\u3000";
        System.out.println("trim 去不掉全角空格: [" + fullWidth.trim() + "]");
        System.out.println("全角空格串的 length: " + fullWidth.length());

        // compareTo 返回的是「首个不同字符的码点差」，前缀相同时返回长度差
        System.out.println("\"abc\".compareTo(\"abd\"): " + "abc".compareTo("abd"));
        System.out.println("\"abc\".compareTo(\"ab\"): " + "abc".compareTo("ab"));
        // 大写字母码点比小写小 32，这正是 toLowerCase 能用位移实现的原因
        System.out.println("\"ABC\".compareTo(\"abc\"): " + "ABC".compareTo("abc"));

        System.out.println("equals 区分大小写: " + "Java".equals("JAVA"));
        System.out.println("equalsIgnoreCase 忽略大小写: " + "Java".equalsIgnoreCase("JAVA"));
    }

    /**
     * ⑥ 替换：replace 是字面量替换，replaceAll 的第一个参数是正则
     */
    public static void replace() {
        // replace 的 char 与 CharSequence 两个重载都是「字面量」替换，且都会替换全部匹配项
        System.out.println("replace('o', 'x'): " + "good".replace('o', 'x'));
        System.out.println("replace(\"o\", \"x\"): " + "good".replace("o", "x"));
        System.out.println("replaceAll(\"o\", \"x\"): " + "good".replaceAll("o", "x"));

        // 两者的差异在正则元字符上暴露：'.' 在 replace 中就是一个点号，在 replaceAll 中却是「任意字符」
        String regexDot = "\\.";
        System.out.println("\"a.b\".replace(\".\", \"-\"): " + "a.b".replace(".", "-"));
        System.out.println("\"a.b\".replaceAll(\".\", \"-\"): " + "a.b".replaceAll(".", "-"));
        System.out.println("\"a.b\".replaceAll(" + regexDot + ", \"-\"): " + "a.b".replaceAll(regexDot, "-"));

        // replaceFirst 只替换第一处匹配
        System.out.println("replaceFirst 只换第一处: " + "a1b2c3".replaceFirst("\\d", "#"));
    }

    /**
     * ⑦ 拆分：split 的三个坑——分隔符是正则、尾部空串被丢弃、limit 的含义
     */
    public static void splitPitfall() {
        System.out.println("按 @ 拆分邮箱: " + Arrays.toString("sample@sina.com".split("@")));

        // 坑一：分隔符是正则，元字符必须转义
        String[] byUnescaped = "a.b.c".split(".");
        System.out.println("\"a.b.c\".split(\".\") 的结果个数: " + byUnescaped.length);
        System.out.println("因为 . 匹配任意字符，整串都被当成分隔符，切出的全是空串又被丢弃");
        System.out.println("\"a.b.c\".split(\"\\\\.\") 的结果: " + Arrays.toString("a.b.c".split("\\.")));

        // 坑二：默认（limit = 0）会丢弃尾部的空串，中间的空串则保留。
        // 处理 CSV 这类「空字段有意义」的数据时，必须传 limit = -1
        System.out.println("\"a,b,c,\".split(\",\") 的结果: " + Arrays.toString("a,b,c,".split(",")));
        System.out.println("\"a,b,c,\".split(\",\", -1) 的结果: " + Arrays.toString("a,b,c,".split(",", -1)));
        System.out.println("中间的空串不会被丢弃: " + Arrays.toString("a,,b".split(",")));

        // 坑三：limit 为正数时最多切成 limit 段，未参与拆分的 remainder 原样留在最后一段
        System.out.println("\"a,b,c,d\".split(\",\", 2) 的结果: " + Arrays.toString("a,b,c,d".split(",", 2)));
        System.out.println("\"a,b,c,\".split(\",\", 0) 的个数: " + "a,b,c,".split(",", 0).length);
        System.out.println("\"a,b,c,\".split(\",\", -1) 的个数: " + "a,b,c,".split(",", -1).length);
    }

    /**
     * ⑧ 拼接与格式化：concat / + / format
     */
    public static void concatAndFormat() {
        String left = "Hello ";
        String right = "World";
        System.out.println("concat: " + left.concat(right));
        System.out.println("+ 拼接: " + (left + right));

        // concat 不接受 null，而 + 会把 null 当成字面量 "null" 拼进去——后者往往掩盖了真正的 bug
        String nothing = null;
        try {
            left.concat(nothing);
        } catch (NullPointerException e) {
            System.out.println("concat(null) 抛出: " + e.getClass().getSimpleName());
        }
        System.out.println("\"Hello \" + null 的结果: [" + (left + nothing) + "]");

        // + 从左到右结合，遇到字符串之前的部分先做算术，之后的部分一律转成字符串再拼
        System.out.println("1 + 2 + \"a\" = " + (1 + 2 + "a"));
        System.out.println("\"a\" + 1 + 2 = " + ("a" + 1 + 2));

        // 需要补零、对齐、限定小数位时，用 format 而不是手工拼接
        System.out.println("format: " + String.format("%s-%03d-%.2f", "No", 7, 3.14159));
    }

    /**
     * 依次演示八组常用 API
     */
    public static void demo() {
        lengthAndCharAt();
        charAndByteConversion();
        search();
        substring();
        trimCaseAndCompare();
        replace();
        splitPitfall();
        concatAndFormat();
    }

    public static void main(String[] args) {
        demo();
    }

}

// Output:
// "Computer".length(): 8
// "Computer".charAt(4): u
// "".isEmpty(): true
// " ".isEmpty(): false
// charAt(length) 抛出: StringIndexOutOfBoundsException
// toCharArray(): [B, a, b, y]
// getChars(0, 6, buffer, 2) 后: __Summer__
// String.valueOf(char[]): ab
// "中" 的 UTF-8 字节: [-28, -72, -83]，共 3 个
// 按 UTF-8 还原: 中
// "中" 的 ISO-8859-1 字节: [63]，共 1 个
// indexOf("o"): 1
// indexOf("o", 5): 9
// lastIndexOf("o"): 9
// lastIndexOf("o", 5): 1
// indexOf("z"): -1
// contains("are"): true
// startsWith("How"): true
// endsWith("you"): true
// startsWith("are", 4): true
// "12345".matches(\d+): true
// "a123".matches(\d+): false
// 想表达「串里有数字」要写成 "a123".matches(.*\d+): true
// substring(6): [World]
// substring(0, 5): [Hello]
// 左闭右开，substring(2, 5): [llo]
// substring(3, 3): []
// endIndex 超出长度抛出: StringIndexOutOfBoundsException
// trim(): [Night]
// toLowerCase(): china
// toUpperCase(): CHINA
// trim 去不掉全角空格: [　Night　]
// 全角空格串的 length: 7
// "abc".compareTo("abd"): -1
// "abc".compareTo("ab"): 1
// "ABC".compareTo("abc"): -32
// equals 区分大小写: false
// equalsIgnoreCase 忽略大小写: true
// replace('o', 'x'): gxxd
// replace("o", "x"): gxxd
// replaceAll("o", "x"): gxxd
// "a.b".replace(".", "-"): a-b
// "a.b".replaceAll(".", "-"): ---
// "a.b".replaceAll(\., "-"): a-b
// replaceFirst 只换第一处: a#b2c3
// 按 @ 拆分邮箱: [sample, sina.com]
// "a.b.c".split(".") 的结果个数: 0
// 因为 . 匹配任意字符，整串都被当成分隔符，切出的全是空串又被丢弃
// "a.b.c".split("\\.") 的结果: [a, b, c]
// "a,b,c,".split(",") 的结果: [a, b, c]
// "a,b,c,".split(",", -1) 的结果: [a, b, c, ]
// 中间的空串不会被丢弃: [a, , b]
// "a,b,c,d".split(",", 2) 的结果: [a, b,c,d]
// "a,b,c,".split(",", 0) 的个数: 3
// "a,b,c,".split(",", -1) 的个数: 4
// concat: Hello World
// + 拼接: Hello World
// concat(null) 抛出: NullPointerException
// "Hello " + null 的结果: [Hello null]
// 1 + 2 + "a" = 3a
// "a" + 1 + 2 = a12
// format: No-007-3.14
