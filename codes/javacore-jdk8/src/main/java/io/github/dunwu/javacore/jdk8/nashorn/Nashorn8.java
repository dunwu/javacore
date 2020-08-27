package io.github.dunwu.javacore.jdk8.nashorn;

import cn.hutool.core.io.resource.ResourceUtil;
import io.github.dunwu.javacore.jdk8.lambda.Person;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Benjamin Winterberg
 */
public class Nashorn8 {

    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        NashornScriptEngine engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
        String filename = ResourceUtil.getResource("META-INF/scripts/nashorn8.js").getFile();
        engine.eval("load('" + filename + "')");

        engine.invokeFunction("evaluate1"); // [object global]
        engine.invokeFunction("evaluate2"); // [object Object]
        engine.invokeFunction("evaluate3", "Foobar"); // Foobar
        engine.invokeFunction("evaluate3", new Person("John", "Doe")); // [object global]
        // <- ???????
    }

}
