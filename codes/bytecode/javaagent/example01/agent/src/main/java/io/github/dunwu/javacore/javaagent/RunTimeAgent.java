package io.github.dunwu.javacore.javaagent;

import java.lang.instrument.Instrumentation;

public class RunTimeAgent {

    public static void premain(String arg, Instrumentation instrumentation) {
        System.out.println("探针启动！！！");
        System.out.println("探针传入参数：" + arg);
        instrumentation.addTransformer(new RunTimeTransformer());
    }

}
