package io.github.dunwu.javacore.enumeration;

/**
 * 错误码常量集（通用方式）
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/24.
 */
public enum ErrorCodeEn {

    OK(0) {
        @Override
        public String getDescription() {
            return "成功";
        }
    },
    ERROR_A(100) {
        @Override
        public String getDescription() {
            return "错误A";
        }
    },
    ERROR_B(200) {
        @Override
        public String getDescription() {
            return "错误B";
        }
    };

    private int code;

    // 构造方法：enum的构造方法只能被声明为private权限或不声明权限
    ErrorCodeEn(int number) { // 构造方法
        this.code = number;
    }

    public static void main(String[] args) { // 静态方法
        for (ErrorCodeEn s : ErrorCodeEn.values()) {
            System.out.println("code: " + s.getCode() + ", description: " + s.getDescription());
        }
    }

    public int getCode() { // 普通方法
        return code;
    } // 普通方法

    public abstract String getDescription(); // 抽象方法
}
// Output:
// code: 0, description: 成功
// code: 100, description: 错误A
// code: 200, description: 错误B
