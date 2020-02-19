package io.github.dunwu.javacore.enumeration;

/**
 * 本例展示枚举状态机
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/24.
 */
public class StateMachineDemo {

    public static void main(String[] args) {
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

}
// Output:
// 红灯停
