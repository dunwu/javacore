package io.github.dunwu.javacore.annotation.custom;

import java.lang.annotation.*;

/**
 * 标记注解 - 字段不能为空
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see NotNullUtil
 * @since 2019-03-31
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

}
