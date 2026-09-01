package io.github.dunwu.javacore.jdk8.time;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Java 8 Instant / Duration / Period 示例。
 * <p>
 * <ul>
 * <li>{@link Instant}：时间线上的一个瞬时点（UTC 纪元毫秒），相当于老 API 的 Date，常用于时间戳</li>
 * <li>{@link Duration}：两个时间点之间的间隔，以"秒 + 纳秒"度量，适合时分秒级别</li>
 * <li>{@link Period}：两个日期之间的间隔，以"年-月-日"度量，适合日历语义（如年龄）</li>
 * </ul>
 */
public class InstantDurationDemo {

    /**
     * 示例 1：Instant 时间戳（UTC），与 LocalDateTime 互转需要指定时区
     */
    public static void instantDemo() {
        Instant instant = Instant.parse("2024-05-20T15:30:45Z");
        System.out.println("instant: " + instant);
        System.out.println("纪元秒: " + instant.getEpochSecond() + ", 毫秒: " + instant.toEpochMilli());
        // Instant <-> LocalDateTime 互转需要指定时区
        LocalDateTime local = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
        System.out.println("转上海本地时间: " + local);
        System.out.println("转回 instant: " + local.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
    }

    /**
     * 示例 2：Duration，时分秒级别的间隔
     */
    public static void durationDemo() {
        Instant start = Instant.parse("2024-05-20T15:00:00Z");
        Instant end = Instant.parse("2024-05-20T15:30:45Z");
        Duration duration = Duration.between(start, end);
        System.out.println("Duration: " + duration + ", 秒数: " + duration.getSeconds());
        System.out.println("加 2 小时: " + start.plus(duration).plus(Duration.ofHours(2)));
        // ChronoUnit 直接算两个时间点的差值
        System.out.println("相差分钟: " + ChronoUnit.MINUTES.between(start, end));
    }

    /**
     * 示例 3：Period，年月日语义的间隔（如年龄）
     */
    public static void periodDemo() {
        LocalDate birthday = LocalDate.of(1995, 3, 15);
        LocalDate today = LocalDate.of(2024, 5, 20);
        Period age = Period.between(birthday, today);
        System.out.println("Period: " + age);
        System.out.println("年龄: " + age.getYears() + " 岁 " + age.getMonths() + " 个月 "
            + age.getDays() + " 天");
        // Period.of 创建后可以做日历加减
        System.out.println("1 年 2 个月 3 天后: " + today.plus(Period.of(1, 2, 3)));
    }

    /**
     * 示例 4：Duration 与 Period 的区别，Duration 是精确时长（30 天 = 720 小时），
     * Period 是日历语义（加 1 个月可能截断到月末）；
     * 注意 LocalDate 不支持直接加 Duration（秒级单位不支持），需用 ChronoUnit
     */
    public static void durationVsPeriod() {
        LocalDate base = LocalDate.of(2024, 1, 31);
        System.out.println("加 30 天（ChronoUnit.DAYS）: " + base.plus(30, ChronoUnit.DAYS));
        System.out.println("Period.ofMonths(1) 加到 2024-01-31: " + base.plus(Period.ofMonths(1)));
    }

    public static void main(String[] args) {
        instantDemo();
        durationDemo();
        periodDemo();
        durationVsPeriod();
    }

}
// Output:
// instant: 2024-05-20T15:30:45Z
// 纪元秒: 1716219045, 毫秒: 1716219045000
// 转上海本地时间: 2024-05-20T23:30:45
// 转回 instant: 2024-05-20T15:30:45Z
// Duration: PT30M45S, 秒数: 1845
// 加 2 小时: 2024-05-20T17:30:45Z
// 相差分钟: 30
// Period: P29Y2M5D
// 年龄: 29 岁 2 个月 5 天
// 1 年 2 个月 3 天后: 2025-07-23
// 加 30 天（ChronoUnit.DAYS）: 2024-03-01
// Period.ofMonths(1) 加到 2024-01-31: 2024-02-29
