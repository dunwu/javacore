package io.github.dunwu.javacore.annotation.standard;
class DeprecatedMethod {
    /**
     * @title print
     * @description 废弃方法
     * @author victor
     * @date 2016年8月2日
     * @return String
     */
    @Deprecated
    public String print() {
        return "DeprecatedMethod";
    }
};


/**
 * @title DeprecatedClass
 * @description 废弃类
 * @author victor
 * @date 2016年8月2日
 */
@Deprecated
class DeprecatedClass {
    public String print() {
        return "DeprecatedClass";
    }
};


public class DeprecatedAnnotationDemo {
    public static void main(String[] args) {
        DeprecatedMethod dm = new DeprecatedMethod();
        System.out.println(dm.print());


        DeprecatedClass dc = new DeprecatedClass();
        System.out.println(dc.print());
    }
};
