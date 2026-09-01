package io.github.dunwu.javacore.control;

/**
 * 分支语句示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see IfDemo
 * @see IfElseDemo
 * @see IfElseifElseDemo
 * @see IfNestedDemo
 * @see SwitchDemo01
 * @see SwitchDemo02
 * @see SwitchDemo03
 */
public class SwitchDemo02 {

    /**
     * 演示 switch（String 类型）：根据成绩等级输出对应的分数范围。
     */
    public static void demo() {
        String level = "及格";
        switch (level) {
            case "优秀": {
                System.out.println("分数范围：>= 90");
                break;
            }
            case "良好": {
                System.out.println("分数范围：>= 80");
                break;
            }
            case "及格": {
                System.out.println("分数范围：>= 60");
                break;
            }
            case "不及格": {
                System.out.println("分数范围：< 60");
                break;
            }
            default: {
                System.out.println("分数等级无效");
                break;
            }
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
// output:
// 分数范围：>= 60
