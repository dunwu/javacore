package io.github.dunwu.javacore.jvm.bytecode;

import javassist.*;

import java.io.IOException;

/**
 * 【错误示例】如果需要修改字节码的类，已经有运行时的实例，这时企图修改字节码时会出错
 * <p>
 * 请留意与 {@link  JavassistDemo} 的区别
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see JavassistDemo
 * @since 2019/10/28
 */
public class JavassistDemo2 {

    public static void main(String[] args)
        throws CannotCompileException, IOException, NotFoundException, IllegalAccessException, InstantiationException {
        Base oldBase = new Base();
        System.out.println("call io.github.dunwu.javacore.jvm.bytecode.Base.process");
        oldBase.process();

        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("io.github.dunwu.javacore.jvm.bytecode.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\"); }");
        m.insertAfter("{ System.out.println(\"end\"); }");
        Class c = cc.toClass();
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        cc.writeFile(classPath + "io/github/dunwu/javacore/jvm/bytecode/");
        Base newBase = (Base) c.newInstance();
        newBase.process();
    }

}
