package io.github.dunwu.javacore.jdk8.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

/**
 * Java 8 新日期时间 API 基础示例（LocalDate / LocalTime / LocalDateTime）。
 * <p>
 * Java 8 引入全新的 {@code java.time} 包（JSR-310），替代老 API（Date/Calendar）：
 * <ul>
 * <li>所有类都是<b>不可变</b>且<b>线程安全</b>的，任何修改操作都返回新对象</li>
 * <li>{@link LocalDate}：只有日期；{@link LocalTime}：只有时间；{@link LocalDateTime}：日期 + 时间（不含时区）</li>
 * <li>{@code plus/minus} 加减、{@code with} 修改某个字段、{@code get} 取值、{@code isBefore/isAfter} 比较</li>
 * </ul>
 */
public class LocalDateDemo {

    /**
     * 示例 1：创建（推荐 of 工厂方法，月份从 1 开始）与字段取值
     */
    public static void createAndAccess() {
        LocalDate date = LocalDate.of(2024, Month.MAY, 20);
        LocalTime time = LocalTime.of(15, 30, 45);
        LocalDateTime dateTime = LocalDateTime.of(2024, 5, 20, 15, 30, 45);
        System.out.println("date: " + date);
        System.out.println("time: " + time);
        System.out.println("dateTime: " + dateTime);

        // 取值
        System.out.println("年: " + date.getYear() + ", 月: " + date.getMonthValue()
            + ", 日: " + date.getDayOfMonth());
        System.out.println("星期: " + date.getDayOfWeek() + ", 一年中的第 " + date.getDayOfYear() + " 天");
    }

    /**
     * 示例 2：不可变性，plus/minus/with 都返回新对象，原对象不变
     */
    public static void immutability() {
        LocalDate date = LocalDate.of(2024, Month.MAY, 20);
        LocalDate tomorrow = date.plusDays(1);
        LocalDate nextMonth = date.plusMonths(1);
        LocalDate lastWeek = date.minusWeeks(1);
        System.out.println("加一天: " + tomorrow + ", 加一月: " + nextMonth + ", 减一周: " + lastWeek);
        System.out.println("原对象不变: " + date);
    }

    /**
     * 示例 3：with 直接设置某个字段
     */
    public static void withField() {
        LocalDate date = LocalDate.of(2024, Month.MAY, 20);
        LocalDate firstDayOfYear = date.withDayOfYear(1);
        System.out.println("当年第一天: " + firstDayOfYear);
    }

    /**
     * 示例 4：比较与距离计算
     */
    public static void compareAndBetween() {
        LocalDate date = LocalDate.of(2024, Month.MAY, 20);
        LocalDate endOfYear = LocalDate.of(2024, 12, 31);
        System.out.println("isBefore: " + date.isBefore(endOfYear));
        System.out.println("相差天数: " + ChronoUnit.DAYS.between(date, endOfYear));
    }

    /**
     * 示例 5：组合与拆分，LocalDate + LocalTime 与 LocalDateTime 互转
     */
    public static void combineAndSplit() {
        LocalDateTime combined = LocalDate.of(2024, 1, 1).atTime(LocalTime.of(8, 0));
        System.out.println("atTime 组合: " + combined);
        LocalDateTime dateTime = LocalDateTime.of(2024, 5, 20, 15, 30, 45);
        System.out.println("toLocalDate 拆分: " + dateTime.toLocalDate() + ", toLocalTime: " + dateTime.toLocalTime());
    }

    /**
     * 示例 6：常用判断（闰年、当月天数、星期枚举比较）
     */
    public static void commonChecks() {
        LocalDate date = LocalDate.of(2024, Month.MAY, 20);
        LocalDate leapDay = LocalDate.of(2024, 2, 29);
        System.out.println("2024 是闰年: " + leapDay.isLeapYear()
            + ", 当月天数: " + leapDay.lengthOfMonth());
        System.out.println("星期几枚举比较: " + (date.getDayOfWeek() == DayOfWeek.MONDAY));
    }

    public static void main(String[] args) {
        createAndAccess();
        immutability();
        withField();
        compareAndBetween();
        combineAndSplit();
        commonChecks();
    }

}
// Output:
// date: 2024-05-20
// time: 15:30:45
// dateTime: 2024-05-20T15:30:45
// 年: 2024, 月: 5, 日: 20
// 星期: MONDAY, 一年中的第 141 天
// 加一天: 2024-05-21, 加一月: 2024-06-20, 减一周: 2024-05-13
// 原对象不变: 2024-05-20
// 当年第一天: 2024-01-01
// isBefore: true
// 相差天数: 225
// atTime 组合: 2024-01-01T08:00
// toLocalDate 拆分: 2024-05-20, toLocalTime: 15:30:45
// 2024 是闰年: true, 当月天数: 29
// 星期几枚举比较: true
