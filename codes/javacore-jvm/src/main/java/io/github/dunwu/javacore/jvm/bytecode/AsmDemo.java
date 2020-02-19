package io.github.dunwu.javacore.jvm.bytecode;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 利用 Asm，修改字节码，实现 AOP
 * <p>
 * 以 {@link Base#process()} 方法为切点，修改其字节码，在方法前后都织入代码
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see <a href="https://asm.ow2.io/asm4-guide.pdf">Asm 4.0 官方文档</a>
 * @since 2019/10/28
 */
public class AsmDemo {

    public static void main(String[] args) throws Exception {
        //读取
        ClassReader classReader = new ClassReader("io/github/dunwu/javacore/jvm/bytecode/Base");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //处理
        ClassVisitor classVisitor = new MyClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();
        //输出
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File f = new File(classPath + "io/github/dunwu/javacore/jvm/bytecode/Base.class");
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(data);
        fout.close();
        Base base = new Base();
        base.process();
    }

}
