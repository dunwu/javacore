package io.github.dunwu.javacore.datatype;

import lombok.Data;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-08-06
 */
public class 枚举判等 {

    public static void main(String[] args) {
        StatusEnum statusEnum = StatusEnum.DELIVERED;
        OrderQuery orderQuery = new OrderQuery(StatusEnum.DELIVERED.status, StatusEnum.DELIVERED.desc);
        System.out.println(statusEnum.status == orderQuery.getStatus());
    }

    @Data
    static class OrderQuery {

        private Integer status;
        private String name;

        public OrderQuery(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

    }

    enum StatusEnum {
        CREATED(1000, "已创建"),
        PAID(1001, "已支付"),
        DELIVERED(1002, "已送到"),
        FINISHED(1003, "已完成");

        private final Integer status;
        private final String desc;

        StatusEnum(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

}
