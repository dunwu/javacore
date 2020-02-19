package io.github.dunwu.javacore.annotation.custom;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-31
 */
public class RegexValidDemo {

    public static void main(String[] args) throws Exception {
        User user = new User("Tom", "1990-01-31", "xxx@163.com", "18612341234");
        User user2 = new User("Jack", "2019-02-29", "sadhgs", "183xxxxxxxx");
        if (RegexValidUtil.check(user)) {
            System.out.println(user + "正则校验通过");
        }
        if (RegexValidUtil.check(user2)) {
            System.out.println(user2 + "正则校验通过");
        }
    }

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
