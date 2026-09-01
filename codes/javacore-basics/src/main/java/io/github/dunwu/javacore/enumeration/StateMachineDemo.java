package io.github.dunwu.javacore.enumeration;

/**
 * 本例展示枚举状态机
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/24.
 */
public class StateMachineDemo {

    /**
     * 演示枚举状态机：根据信号灯状态输出对应指令。
     */
    public static void demo() {
        System.out.println(getTrafficInstruct(Signal.RED));
    }

    public static String getTrafficInstruct(Signal signal) {
        String instruct = "信号灯故障";
        switch (signal) {
            case RED:
                instruct = "红灯停";
                break;
            case YELLOW:
                instruct = "黄灯请注意";
                break;
            case GREEN:
                instruct = "绿灯行";
                break;
            default:
                break;
        }
        return instruct;
    }

    public enum Signal {

        GREEN,
        YELLOW,
        RED
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// 红灯停
