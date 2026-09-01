package io.github.dunwu.javacore.jdk8.time;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Java 8 日期调整器（TemporalAdjusters）与时区 API 示例。
 * <p>
 * <ul>
 * <li>{@link TemporalAdjusters}：提供"下一个工作日"、"本月最后一天"等复杂日期计算，
 * 也可以用 lambda 自定义调整器</li>
 * <li>{@link ZonedDateTime}：带时区的日期时间，处理跨时区转换</li>
 * <li>新老 API 互转：{@code Date <-> Instant}、{@code Date <-> LocalDateTime}</li>
 * </ul>
 */
public class TemporalAdjusterDemo {

    /**
     * 示例 1：内置调整器
     */
    public static void builtinAdjusters() {
        LocalDate date = LocalDate.of(2024, 5, 20); // 星期一
        System.out.println("本月第一天: " + date.with(TemporalAdjusters.firstDayOfMonth()));
        System.out.println("本月最后一天: " + date.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println("下个月第一天: " + date.with(TemporalAdjusters.firstDayOfNextMonth()));
        System.out.println("下一个周五: " + date.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));
        System.out.println("上一个周五: " + date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)));
    }

    /**
     * 示例 2：自定义调整器，下一个工作日（跳过周末）
     */
    public static void customAdjuster() {
        LocalDate date = LocalDate.of(2024, 5, 20); // 星期一
        LocalDate nextWorkday = date.with(temporal -> {
            LocalDate d = LocalDate.from(temporal).plusDays(1);
            while (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) {
                d = d.plusDays(1);
            }
            return d;
        });
        System.out.println("下一个工作日: " + nextWorkday);
    }

    /**
     * 示例 3：ZonedDateTime，带时区的日期时间与跨时区转换
     */
    public static void zonedDateTime() {
        ZonedDateTime shanghai = ZonedDateTime.of(2024, 5, 20, 15, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        System.out.println("上海时间: " + shanghai);
        // 同一时刻转纽约时间（自动处理时差）
        ZonedDateTime newYork = shanghai.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println("同一时刻纽约时间: " + newYork);
        System.out.println("时区偏移: " + shanghai.getOffset());
    }

    /**
     * 示例 4：新老 API 互转（Date 与 Instant/LocalDateTime）
     */
    public static void legacyConversion() {
        // Date <-> Instant
        Date legacyDate = Date.from(Instant.parse("2024-05-20T15:30:45Z"));
        Instant backToInstant = legacyDate.toInstant();
        System.out.println("Date -> Instant: " + backToInstant);
        // Date -> LocalDateTime（需经 Instant + 时区）
        System.out.println("Date -> LocalDateTime: "
            + backToInstant.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime());
    }

    public static void main(String[] args) {
        builtinAdjusters();
        customAdjuster();
        zonedDateTime();
        legacyConversion();
    }

}
// Output:
// 本月第一天: 2024-05-01
// 本月最后一天: 2024-05-31
// 下个月第一天: 2024-06-01
// 下一个周五: 2024-05-24
// 上一个周五: 2024-05-17
// 下一个工作日: 2024-05-21
// 上海时间: 2024-05-20T15:30+08:00[Asia/Shanghai]
// 同一时刻纽约时间: 2024-05-20T03:30-04:00[America/New_York]
// 时区偏移: +08:00
// Date -> Instant: 2024-05-20T15:30:45Z
// Date -> LocalDateTime: 2024-05-20T23:30:45
