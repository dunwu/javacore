package io.github.dunwu.javacore.jvm.bytecode;

import javassist.*;

import java.io.IOException;

/**
 * 利用 Javassist，修改字节码，实现 AOP
 * <p>
 * 以 {@link Base#process()} 方法为切点，修改其字节码，在方法前后都织入代码
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019/10/28
 */
public class JavassistDemo {

    public static void main(String[] args)
        throws CannotCompileException, IOException, NotFoundException, IllegalAccessException, InstantiationException {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("io.github.dunwu.javacore.jvm.bytecode.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\"); }");
        m.insertAfter("{ System.out.println(\"end\"); }");
        Class c = cc.toClass();
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        cc.writeFile(classPath + "io/github/dunwu/javacore/jvm/bytecode/");
        Base base = (Base) c.newInstance();
        base.process();
    }

}
