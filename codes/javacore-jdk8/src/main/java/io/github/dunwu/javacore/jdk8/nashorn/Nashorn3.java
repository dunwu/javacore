package io.github.dunwu.javacore.jdk8.nashorn;

import cn.hutool.core.io.resource.ResourceUtil;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Working with java types from javascript.
 *
 * @author Benjamin Winterberg
 */
public class Nashorn3 {

    public static void main(String[] args) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        BufferedReader reader = ResourceUtil.getReader("META-INF/scripts/nashorn3.js", Charset.defaultCharset());
        engine.eval(reader);
    }

}
