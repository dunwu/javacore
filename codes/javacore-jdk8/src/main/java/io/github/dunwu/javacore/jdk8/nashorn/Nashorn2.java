package io.github.dunwu.javacore.jdk8.nashorn;

import cn.hutool.core.io.resource.ResourceUtil;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Calling java methods from javascript with nashorn.
 *
 * @author Benjamin Winterberg
 */
public class Nashorn2 {

    public static String fun(String name) {
        System.out.format("Hi there from Java, %s", name);
        return "greetings from java";
    }

    public static void fun2(Object object) {
        System.out.println(object.getClass());
    }

    public static void fun3(ScriptObjectMirror mirror) {
        System.out.println(mirror.getClassName() + ": " + Arrays.toString(mirror.getOwnKeys(true)));
    }

    public static void fun4(ScriptObjectMirror person) {
        System.out.println("Full Name is: " + person.callMember("getFullName"));
    }

    public static void main(String[] args) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        BufferedReader reader = ResourceUtil.getReader("META-INF/scripts/nashorn2.js", Charset.defaultCharset());
        engine.eval(reader);
    }

}
