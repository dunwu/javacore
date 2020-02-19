package io.github.dunwu.javacore.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Amazon implements InvocationHandler {

    private Purchaser consumer;

    public Amazon(Purchaser consumer) {
        this.consumer = consumer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("亚马逊代购");
        return method.invoke(consumer, args);
    }

}
