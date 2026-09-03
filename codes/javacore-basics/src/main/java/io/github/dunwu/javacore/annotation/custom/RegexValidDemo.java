package io.github.dunwu.javacore.annotation.custom;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-31
 */
public class RegexValidDemo {

    /**
     * 演示自定义注解 {@code @RegexValid} 的运行期正则校验
     * <p>
     * 注解支持两种配置：内置策略 {@code policy}（DATE / MAIL）与自定义正则 {@code value}。
     * {@link RegexValidUtil} 通过反射读取字段注解，{@code value} 为空串时回退到 {@code policy} 的正则，
     * 再用 {@code Pattern.matcher().matches()} 校验字段值；校验不通过时抛出汇总了全部错误信息的异常
     */
    public static void demo() {
        // 正例：日期、邮箱、手机号均合法
        check(new User("Tom", "1990-01-31", "xxx@163.com", "18612341234"));
        // 反例：2019 年不是闰年（2 月无 29 日）、邮箱缺少 @、手机号含非数字字符
        check(new User("Jack", "2019-02-29", "sadhgs", "183xxxxxxxx"));
    }

    private static void check(User user) {
        try {
            if (RegexValidUtil.check(user)) {
                System.out.println(user + " 正则校验通过");
            }
        } catch (Exception e) {
            System.out.println("正则校验失败，原因:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        demo();
    }

    /**
     * {@code @RegexValid} 的 {@code @Target} 允许修饰方法参数，但本示例的 {@link RegexValidUtil}
     * 只用反射处理字段，参数上的注解不会被校验——再次说明注解必须由处理器配合才产生行为
     */
    static void printDate(@RegexValid(policy = RegexValid.Policy.DATE) String date) {
        System.out.println(date);
    }

    static class User {

        private String name;

        @RegexValid(policy = RegexValid.Policy.DATE)
        private String date;

        @RegexValid(policy = RegexValid.Policy.MAIL)
        private String mail;

        @RegexValid("^((\\+)?86\\s*)?((13[0-9])|(15([0-3]|[5-9]))|(18[0,2,5-9]))\\d{8}$")
        private String phone;

        public User(String name, String date, String mail, String phone) {
            this.name = name;
            this.date = date;
            this.mail = mail;
            this.phone = phone;
        }

        @Override
        public String toString() {
            return "User{" + "name='" + name + '\'' + ", date='" + date + '\'' + ", mail='" + mail + '\'' + ", phone='"
                + phone + '\'' + '}';
        }

    }

}
