package io.github.dunwu.javacore.jdk8.nashorn;

import cn.hutool.core.io.resource.ResourceUtil;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import java.util.concurrent.TimeUnit;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Benjamin Winterberg
 */
public class Nashorn9 {

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        NashornScriptEngine engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        String filename = ResourceUtil.getResource("META-INF/scripts/nashorn9.js").getFile();
        engine.eval("load('" + filename + "')");

        long t0 = System.nanoTime();

        double result = 0;
        for (int i = 0; i < 1000; i++) {
            double num = (double) engine.invokeFunction("testPerf");
            result += num;
        }

        System.out.println(result > 0);

        long took = System.nanoTime() - t0;
        System.out.format("Elapsed time: %d ms", TimeUnit.NANOSECONDS.toMillis(took));
    }

}
