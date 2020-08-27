package io.github.dunwu.javacore.jdk8.nashorn;

import cn.hutool.core.io.resource.ResourceUtil;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Working with java types from javascript.
 *
 * @author Benjamin Winterberg
 */
public class Nashorn4 {

    public static void main(String[] args) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String filename = ResourceUtil.getResource("META-INF/scripts/nashorn4.js").getFile();
        engine.eval("loadWithNewGlobal('" + filename + "')");
    }

}
