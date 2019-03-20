package io.github.dunwu.javacore.annotation.standard;
class SuppressDemo<T> {
    private T var;

    public T getVar() {
        return this.var;
    }

    public void setVar(T var) {
        this.var = var;
    }
};


public class SuppressWarningsAnnotationDemo {
    /**
     * @title main
     * @description 抑制警告
     * @author victor
     * @date 2016年8月2日
     * @return void
     * @param args
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {
        SuppressDemo d = new SuppressDemo();
        d.setVar("南京");
        System.out.println("地名：" + d.getVar());
    }
};
