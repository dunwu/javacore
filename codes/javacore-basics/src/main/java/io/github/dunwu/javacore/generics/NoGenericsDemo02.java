package io.github.dunwu.javacore.generics;

/**
 * 不使用泛型的隐患示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see GenericsClassDemo01
 * @since 2019-03-20
 */
public class NoGenericsDemo02 {

    public static void main(String[] args) {
        Info info = new Info();
        info.setValue("abc");
        String str = (String) info.getValue();
        System.out.println("str = [" + str + "]");
    }

    static class Info {

        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }

}
// Output:
// obj1 = [abc]
// obj2 = [18]
// obj3 = [[D@47089e5f]
// Exception in thread "main" java.lang.ClassCastException: java.lang.String cannot be
// cast to java.lang.Integer
// at io.github.dunwu.javacore.generics.NoGenericsDemo.main(NoGenericsDemo.java:23)
