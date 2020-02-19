package io.github.dunwu.javacore.annotation.custom;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-31
 */
public class NotNullDemo {

    public static void main(String[] args) throws IllegalAccessException {
        MyBean myBean = new MyBean(null, "jack");
        NotNullUtil.check(myBean);
    }

    static class MyBean {

        @NotNull
        private Integer id;

        private String name;

        public MyBean(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
