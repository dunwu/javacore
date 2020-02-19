package io.github.dunwu.javacore.annotation.custom;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-31
 */
@Documented
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface RegexValid {

    String value() default "";

    Policy policy() default Policy.EMPTY;

    enum Policy {

        // @formatter:off
        EMPTY(null),
        DATE("^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1"
            + "(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|"
            + "(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\2(?:29))$"),
        MAIL("^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$");
        // @formatter:on

        private String policy;

        Policy(String policy) {
            this.policy = policy;
        }

        public String getPolicy() {
            return policy;
        }
    }

}
