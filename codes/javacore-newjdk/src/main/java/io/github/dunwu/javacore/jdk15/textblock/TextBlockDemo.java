package io.github.dunwu.javacore.jdk15.textblock;

/**
 * Java 15 文本块（Text Blocks）示例。
 * <p>
 * Java 13/14 预览、Java 15 正式转正。文本块使用三引号 {@code """} 包裹，
 * 用于书写多行字符串（HTML、SQL、JSON 等），免去转义和拼接的噪音：
 * <ul>
 * <li>开始标记 {@code """} 后必须换行</li>
 * <li>结束标记的位置决定"公共缩进"，编译器会自动去除（incidental indentation）</li>
 * <li>无需转义双引号</li>
 * <li>{@code \} 在行尾表示续行（去掉换行符），{@code \s} 表示显式空格（防止行尾空白被删除）</li>
 * </ul>
 */
public class TextBlockDemo {

    /**
     * 示例 1：JSON 文本块与传统拼接写法等价，所见即所得
     */
    public static void jsonTextBlock() {
        // 传统写法：拼接 + 转义，可读性差
        String oldJson = "{\n" +
            "  \"name\": \"Java\",\n" +
            "  \"version\": 15\n" +
            "}";

        // 文本块写法：所见即所得
        String json = """
            {
              "name": "Java",
              "version": 15
            }""";
        System.out.println("JSON 文本块与旧写法等价: " + oldJson.equals(json));
        System.out.println(json);
    }

    /**
     * 示例 2：SQL 文本块——无需转义单引号、双引号
     */
    public static void sqlTextBlock() {
        String sql = """
            SELECT id, name, age
            FROM t_user
            WHERE age >= 18
              AND name LIKE '%Java%'
            ORDER BY id DESC""";
        System.out.println("SQL 文本块:");
        System.out.println(sql);
    }

    /**
     * 示例 3：HTML 文本块——结束标记缩进控制公共缩进的去除量
     */
    public static void htmlTextBlock() {
        String html = """
                <html>
                    <body>
                        <p>Hello, 文本块</p>
                    </body>
                </html>
            """;
        System.out.println("HTML 文本块:");
        System.out.println(html);
    }

    /**
     * 示例 4：转义序列——行尾 \ 续行、\s 显式空格
     */
    public static void escapeSequences() {
        // 行尾 \ 表示续行（去掉换行符）
        String joined = """
            红色 \
            绿色 \
            蓝色""";
        System.out.println("续行结果: " + joined);

        // \s 显式空格：保留行尾空格，且补齐该行长度
        String padded = """
            red  \s
            green\s
            blue \s""";
        System.out.println("每行补齐后的长度是否一致: "
            + (padded.lines().mapToInt(String::length).distinct().count() == 1));
    }

    public static void main(String[] args) {
        jsonTextBlock();
        sqlTextBlock();
        htmlTextBlock();
        escapeSequences();
    }

}
