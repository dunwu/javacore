package io.github.dunwu.javacore.jdk8.nashorn;

import cn.hutool.core.io.resource.ResourceUtil;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Using Backbone Models from Nashorn.
 *
 * @author Benjamin Winterberg
 */
public class Nashorn6 {

    public static void getProduct(ScriptObjectMirror result) {
        System.out.println(result.get("name") + ": " + result.get("valueOfGoods"));
    }

    public static void main(String[] args) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String filename = ResourceUtil.getResource("META-INF/scripts/nashorn6.js").getFile();
        engine.eval("load('" + filename + "')");

        Invocable invocable = (Invocable) engine;

        Product product = new Product();
        product.setName("Rubber");
        product.setPrice(1.99);
        product.setStock(1337);

        ScriptObjectMirror result = (ScriptObjectMirror) invocable.invokeFunction("calculate", product);
        System.out.println(result.get("name") + ": " + result.get("valueOfGoods"));
    }

}
