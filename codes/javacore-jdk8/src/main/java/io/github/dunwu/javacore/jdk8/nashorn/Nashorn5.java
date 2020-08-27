package io.github.dunwu.javacore.jdk8.nashorn;

import cn.hutool.core.io.resource.ResourceUtil;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Bind java objects to custom javascript objects.
 *
 * @author Benjamin Winterberg
 */
public class Nashorn5 {

    public static void main(String[] args) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String filename = ResourceUtil.getResource("META-INF/scripts/nashorn5.js").getFile();
        engine.eval("load('" + filename + "')");

        Invocable invocable = (Invocable) engine;

        Product product = new Product();
        product.setName("Rubber");
        product.setPrice(1.99);
        product.setStock(1037);

        Object result = invocable.invokeFunction("getValueOfGoods", product);
        System.out.println(result);
    }

}
