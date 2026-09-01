package io.github.dunwu.javacore.jdk14.switchstmt;

import java.time.DayOfWeek;

/**
 * Java 14 Switch 表达式示例。
 * <p>
 * switch 在 Java 12 预览、Java 14 正式转正为"表达式"（switch expression），
 * 除了传统语句形式外，还可以直接返回值：
 * <ul>
 * <li>箭头语法（{@code case X ->}）：无穿透（fall-through），无需 break</li>
 * <li>一个分支可以匹配多个标签：{@code case A, B ->}</li>
 * <li>使用 {@code yield} 从代码块分支中返回值</li>
 * <li>switch 表达式的分支必须穷举所有可能值（配合枚举或 sealed 类型更安全）</li>
 * </ul>
 */
public class SwitchExpressionDemo {

    /**
     * 示例 1：箭头语法——单行表达式分支，无穿透、无需 break
     */
    public static void arrowSyntax() {
        DayOfWeek day = DayOfWeek.WEDNESDAY;
        boolean isWeekend = switch (day) {
            case SATURDAY, SUNDAY -> true;
            default -> false;
        };
        System.out.println(day + " 是周末吗: " + isWeekend);
    }

    /**
     * 示例 2：yield——分支是代码块时，用 yield 返回值
     */
    public static void yieldDemo() {
        DayOfWeek day = DayOfWeek.WEDNESDAY;
        int numLetters = switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> 6;
            case TUESDAY -> 7;
            case THURSDAY, SATURDAY -> 8;
            case WEDNESDAY -> {
                System.out.println("  （分支内可以有多条语句，用 yield 返回）");
                yield 9;
            }
        };
        System.out.println(day + " 的英文字母数: " + numLetters);
    }

    /**
     * 示例 3：传统 switch 语句对比（仍然合法）
     */
    public static void oldStyleSwitch() {
        String dayType = getDayTypeOldStyle(DayOfWeek.WEDNESDAY);
        System.out.println("传统 switch 结果: " + dayType);
    }

    public static void main(String[] args) {
        arrowSyntax();
        yieldDemo();
        oldStyleSwitch();
    }

    /**
     * 传统 switch 语句写法：需要 break 防止穿透
     */
    private static String getDayTypeOldStyle(DayOfWeek day) {
        String type;
        switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                type = "工作日";
                break;
            case SATURDAY:
            case SUNDAY:
                type = "周末";
                break;
            default:
                throw new IllegalStateException("未知日期: " + day);
        }
        return type;
    }

}
// Output:
// WEDNESDAY 是周末吗: false
//   （分支内可以有多条语句，用 yield 返回）
// WEDNESDAY 的英文字母数: 9
// 传统 switch 结果: 工作日
