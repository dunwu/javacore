package io.github.dunwu.javacore.datatype;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * {@link StringDemo} 单元测试：String 常用 API。
 * <p>
 * 本类原先没有对应的主类，测试直接对字符串字面量做断言，且有一个完全没被测试覆盖的 {@code main} 方法。
 * 现已把那些覆盖点收拢进 {@link StringDemo} 的具名子方法，并统一改用 AssertJ。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("String 常用 API 示例测试")
public class StringDemoTest {

    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("取值：length / charAt / isEmpty，索引越界抛异常")
    void testLengthAndCharAt() {
        String output = captureOutput(StringDemo::lengthAndCharAt);
        assertThat(output).isEqualTo("\"Computer\".length(): 8\n"
            + "\"Computer\".charAt(4): u\n"
            + "\"\".isEmpty(): true\n"
            // 只含空格的串不是空串
            + "\" \".isEmpty(): false\n"
            + "charAt(length) 抛出: StringIndexOutOfBoundsException\n");
    }

    @Test
    @DisplayName("转换：toCharArray / getChars / getBytes，字符集必须显式指定")
    void testCharAndByteConversion() {
        String output = captureOutput(StringDemo::charAndByteConversion);
        assertThat(output).isEqualTo("toCharArray(): [B, a, b, y]\n"
            + "getChars(0, 6, buffer, 2) 后: __Summer__\n"
            + "String.valueOf(char[]): ab\n"
            + "\"中\" 的 UTF-8 字节: [-28, -72, -83]，共 3 个\n"
            + "按 UTF-8 还原: 中\n"
            // ISO-8859-1 装不下汉字，无法映射的字符被替换成 '?'（63）
            + "\"中\" 的 ISO-8859-1 字节: [63]，共 1 个\n");
    }

    @Test
    @DisplayName("查找：indexOf 系列未命中返回 -1，matches 是整串匹配")
    void testSearch() {
        String output = captureOutput(StringDemo::search);
        assertThat(output).isEqualTo("indexOf(\"o\"): 1\n"
            + "indexOf(\"o\", 5): 9\n"
            + "lastIndexOf(\"o\"): 9\n"
            + "lastIndexOf(\"o\", 5): 1\n"
            + "indexOf(\"z\"): -1\n"
            + "contains(\"are\"): true\n"
            + "startsWith(\"How\"): true\n"
            + "endsWith(\"you\"): true\n"
            + "startsWith(\"are\", 4): true\n"
            + "\"12345\".matches(\\d+): true\n"
            // 关键结论：matches 要求整串匹配，前面多一个字母就不匹配了
            + "\"a123\".matches(\\d+): false\n"
            + "想表达「串里有数字」要写成 \"a123\".matches(.*\\d+): true\n");
    }

    @Test
    @DisplayName("截取：substring 区间左闭右开，begin == end 得到空串")
    void testSubstring() {
        String output = captureOutput(StringDemo::substring);
        assertThat(output).isEqualTo("substring(6): [World]\n"
            + "substring(0, 5): [Hello]\n"
            + "左闭右开，substring(2, 5): [llo]\n"
            + "substring(3, 3): []\n"
            + "endIndex 超出长度抛出: StringIndexOutOfBoundsException\n");
    }

    @Test
    @DisplayName("修剪与比较：trim 去不掉全角空格，compareTo 返回码点差")
    void testTrimCaseAndCompare() {
        String output = captureOutput(StringDemo::trimCaseAndCompare);
        assertThat(output).isEqualTo("trim(): [Night]\n"
            + "toLowerCase(): china\n"
            + "toUpperCase(): CHINA\n"
            // 关键结论：trim 只处理码点 <= U+0020 的字符，全角空格 U+3000 原样保留
            + "trim 去不掉全角空格: [\u3000Night\u3000]\n"
            + "全角空格串的 length: 7\n"
            + "\"abc\".compareTo(\"abd\"): -1\n"
            + "\"abc\".compareTo(\"ab\"): 1\n"
            + "\"ABC\".compareTo(\"abc\"): -32\n"
            + "equals 区分大小写: false\n"
            + "equalsIgnoreCase 忽略大小写: true\n");
    }

    @Test
    @DisplayName("替换：replace 按字面量替换，replaceAll 的第一个参数是正则")
    void testReplace() {
        String output = captureOutput(StringDemo::replace);
        assertThat(output).isEqualTo("replace('o', 'x'): gxxd\n"
            + "replace(\"o\", \"x\"): gxxd\n"
            + "replaceAll(\"o\", \"x\"): gxxd\n"
            + "\"a.b\".replace(\".\", \"-\"): a-b\n"
            // 关键结论：同样的 "."，在 replaceAll 中是「任意字符」，整串都被替换掉了
            + "\"a.b\".replaceAll(\".\", \"-\"): ---\n"
            + "\"a.b\".replaceAll(\\., \"-\"): a-b\n"
            + "replaceFirst 只换第一处: a#b2c3\n");
    }

    @Test
    @DisplayName("拆分：split 的分隔符是正则、尾部空串被丢弃、limit 控制段数")
    void testSplitPitfall() {
        String output = captureOutput(StringDemo::splitPitfall);
        assertThat(output).isEqualTo("按 @ 拆分邮箱: [sample, sina.com]\n"
            // 坑一："." 是正则元字符，整串都被当成分隔符，结果一个元素都没有
            + "\"a.b.c\".split(\".\") 的结果个数: 0\n"
            + "因为 . 匹配任意字符，整串都被当成分隔符，切出的全是空串又被丢弃\n"
            + "\"a.b.c\".split(\"\\\\.\") 的结果: [a, b, c]\n"
            // 坑二：尾部空串默认被丢弃，limit = -1 才保留
            + "\"a,b,c,\".split(\",\") 的结果: [a, b, c]\n"
            + "\"a,b,c,\".split(\",\", -1) 的结果: [a, b, c, ]\n"
            + "中间的空串不会被丢弃: [a, , b]\n"
            // 坑三：limit 为正数时，剩余部分原样留在最后一段
            + "\"a,b,c,d\".split(\",\", 2) 的结果: [a, b,c,d]\n"
            + "\"a,b,c,\".split(\",\", 0) 的个数: 3\n"
            + "\"a,b,c,\".split(\",\", -1) 的个数: 4\n");
    }

    @Test
    @DisplayName("拼接与格式化：concat 不接受 null，+ 把 null 当成 \"null\"")
    void testConcatAndFormat() {
        String output = captureOutput(StringDemo::concatAndFormat);
        assertThat(output).isEqualTo("concat: Hello World\n"
            + "+ 拼接: Hello World\n"
            + "concat(null) 抛出: NullPointerException\n"
            + "\"Hello \" + null 的结果: [Hello null]\n"
            // + 从左到右结合：遇到字符串之前先做算术，之后一律转成字符串再拼
            + "1 + 2 + \"a\" = 3a\n"
            + "\"a\" + 1 + 2 = a12\n"
            + "format: No-007-3.14\n");
    }

    @Test
    @DisplayName("demo：完整演示可正常执行")
    void testDemo() {
        // 用 captureOutput 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> captureOutput(StringDemo::demo)).doesNotThrowAnyException();
    }

}
