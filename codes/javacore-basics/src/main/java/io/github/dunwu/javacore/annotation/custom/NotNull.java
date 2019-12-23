package io.github.dunwu.javacore.annotation.custom;

import java.lang.annotation.*;

/**
 * 标记注解 - 字段不能为空
 *
 * @author Zhang Peng
 * @since 2019-03-31
 * @see NotNullUtil
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

}
