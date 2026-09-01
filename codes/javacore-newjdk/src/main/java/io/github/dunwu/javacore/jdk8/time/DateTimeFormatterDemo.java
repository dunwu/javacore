package io.github.dunwu.javacore.jdk8.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Java 8 日期时间格式化（DateTimeFormatter）示例。
 * <p>
 * {@link DateTimeFormatter} 替代老的 SimpleDateFormat：
 * <ul>
 * <li>线程安全（SimpleDateFormat 非线程安全，是著名并发坑）</li>
 * <li>内置 ISO 标准格式，也支持自定义 pattern</li>
 * <li>格式化：{@code formatter.format(dateTime)} 或 {@code dateTime.format(formatter)}</li>
 * <li>解析：{@code LocalDateTime.parse(text, formatter)}</li>
 * </ul>
 * 常用 pattern：yyyy 年、MM 月、dd 日、HH 24 小时制、mm 分、ss 秒、SSS 毫秒、E 星期。
 */
public class DateTimeFormatterDemo {

    /**
     * 示例 1：内置 ISO 格式
     */
    public static void isoFormat() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 5, 20, 15, 30, 45);
        System.out.println("ISO_LOCAL_DATE_TIME: " + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateTime));
        System.out.println("ISO_LOCAL_DATE: " + dateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    /**
     * 示例 2：自定义 pattern
     */
    public static void customPattern() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 5, 20, 15, 30, 45);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("自定义格式化: " + dateTime.format(formatter));
        System.out.println("中文格式: " + dateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分")));
    }

    /**
     * 示例 3：本地化风格（显式固定 Locale，避免运行环境差异）
     */
    public static void localizedStyle() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 5, 20, 15, 30, 45);
        System.out.println("MEDIUM 风格: " + dateTime.format(
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.CHINA)));
    }

    /**
     * 示例 4：解析，字符串转日期时间对象
     */
    public static void parseDemo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsed = LocalDateTime.parse("2024-05-20 15:30:45", formatter);
        System.out.println("解析结果: " + parsed);
        // 不带 formatter 时默认按 ISO 格式解析
        System.out.println("ISO 解析: " + LocalDateTime.parse("2024-05-20T15:30:45"));
    }

    /**
     * 示例 5：格式与解析互逆
     */
    public static void roundTrip() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 5, 20, 15, 30, 45);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String text = formatter.format(dateTime);
        System.out.println("format -> parse 往返一致: " + LocalDateTime.parse(text, formatter).equals(dateTime));
    }

    public static void main(String[] args) {
        isoFormat();
        customPattern();
        localizedStyle();
        parseDemo();
        roundTrip();
    }

}
// Output:
// ISO_LOCAL_DATE_TIME: 2024-05-20T15:30:45
// ISO_LOCAL_DATE: 2024-05-20
// 自定义格式化: 2024-05-20 15:30:45
// 中文格式: 2024年05月20日 15时30分
// MEDIUM 风格: 2024年5月20日 15:30:45
// 解析结果: 2024-05-20T15:30:45
// ISO 解析: 2024-05-20T15:30:45
// format -> parse 往返一致: true
