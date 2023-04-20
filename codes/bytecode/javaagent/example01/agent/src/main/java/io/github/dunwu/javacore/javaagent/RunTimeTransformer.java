package io.github.dunwu.javacore.javaagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class RunTimeTransformer implements ClassFileTransformer {

    private static final String INJECTED_CLASS = "io.github.dunwu.javacore.javaagent.AppInit";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String realClassName = className.replace("/", ".");
        if (realClassName.equals(INJECTED_CLASS)) {
            System.out.println("拦截到的类名：" + realClassName);
            CtClass ctClass;
            try {
                // 使用javassist,获取字节码类
                ClassPool classPool = ClassPool.getDefault();
                ctClass = classPool.get(realClassName);

                // 得到该类所有的方法实例，也可选择方法，进行增强
                CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
                for (CtMethod method : declaredMethods) {
                    System.out.println(method.getName() + "方法被拦截");
                    method.addLocalVariable("time", CtClass.longType);
                    method.insertBefore("System.out.println(\"---开始执行---\");");
                    method.insertBefore("time = System.currentTimeMillis();");
                    method.insertAfter("System.out.println(\"---结束执行---\");");
                    method.insertAfter("System.out.println(\"运行耗时: \" + (System.currentTimeMillis() - time));");
                }
                return ctClass.toBytecode();
            } catch (Throwable e) { //这里要用Throwable，不要用Exception
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }

}
