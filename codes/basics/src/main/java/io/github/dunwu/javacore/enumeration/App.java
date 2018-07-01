/**
 * The Apache License 2.0
 * Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.enumeration;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Zhang Peng
 * @date 2016/11/24.
 * @see org.zp.javase.enumeration.PayrollDay
 */
public class App {
    public static void main(String[] args) {
        // 测试 PayrollDay （策略枚举）
        System.out.println("时薪100的人在周五工作8小时的收入：" + PayrollDay.FRIDAY.pay(8.0, 100));
        System.out.println("时薪100的人在周六工作8小时的收入：" + PayrollDay.SATURDAY.pay(8.0, 100));

        // EnumSet的使用
        System.out.println("EnumSet展示");
        EnumSet<ErrorCodeEn> errSet = EnumSet.allOf(ErrorCodeEn.class);
        for (ErrorCodeEn e : errSet) {
            System.out.println(e.name() + " : " + e.ordinal());
        }

        // EnumMap的使用
        System.out.println("EnumMap展示");
        EnumMap<StateMachine.Signal, String> errMap = new EnumMap(StateMachine.Signal.class);
        errMap.put(StateMachine.Signal.RED, "红灯");
        errMap.put(StateMachine.Signal.YELLOW, "黄灯");
        errMap.put(StateMachine.Signal.GREEN, "绿灯");
        for (Iterator<Map.Entry<StateMachine.Signal, String>> iter = errMap.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<StateMachine.Signal, String> entry = iter.next();
            System.out.println(entry.getKey().name() + " : " + entry.getValue());
        }
    }
}
