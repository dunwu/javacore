package io.github.dunwu.javacore.javaagent;

import java.lang.instrument.Instrumentation;

/**
 * agentmain 在 main 函数开始运行后才启动（依赖于Attach机制）
 */
public class RunTimeAgent {

    public static void agentmain(String arg, Instrumentation instrumentation) {
        System.out.println("agentmain探针启动！！！");
        System.out.println("agentmain探针传入参数：" + arg);
        instrumentation.addTransformer(new RunTimeTransformer());
    }

}
