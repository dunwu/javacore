package io.github.dunwu.javacore.bytecode.asm;

import io.github.dunwu.javacore.bytecode.Base;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 通过 ASM 进行字节码增强，实现一个简单的 AOP
 * <p>
 * 说明：{@link MyClassVisitor} 继承自 {@link ClassVisitor}，用于对字节码的观察，以 {@link Base#process()} 方法为切点，修改其字节码，在方法前后都织入代码
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see <a href="https://asm.ow2.io/asm4-guide.pdf">Asm 4.0 官方文档</a>
 * @since 2019/10/28
 */
public class MyClassVisitor extends ClassVisitor implements Opcodes {

    public MyClassVisitor(ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
        String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    /**
     * 步骤 1. 通过 visitMethod 方法，判断当前字节码读到哪一个方法了。跳过构造方法 <init> 后，将需要被增强的方法交给内部类 MyMethodVisitor 来进行处理。
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        // Base类中有两个方法：无参构造以及 process 方法，这里不增强构造方法
        if (!name.equals("<init>") && mv != null) {
            mv = new MyMethodVisitor(mv);
        }
        return mv;
    }

    static class MyMethodVisitor extends MethodVisitor implements Opcodes {

        public MyMethodVisitor(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        /**
         * 步骤 2. 本方法会在 ASM 开始访问某一个方法的 Code 区时被调用，重写 visitCode 方法，将 AOP 中的前置逻辑就放在这里。
         */
        @Override
        public void visitCode() {
            super.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("start");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }

        /**
         * 步骤 3. MyMethodVisitor 继续读取字节码指令，每当 ASM 访问到无参数指令时，都会调用 MyMethodVisitor 中的 visitInsn 方法。
         * <p>
         * 我们判断了当前指令是否为无参数的 return 指令，如果是就在它的前面添加一些指令，也就是将 AOP 的后置逻辑放在该方法中。
         */
        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)
                || opcode == Opcodes.ATHROW) {
                //方法在返回之前，打印"end"
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitLdcInsn("end");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            mv.visitInsn(opcode);
        }

    }

}
