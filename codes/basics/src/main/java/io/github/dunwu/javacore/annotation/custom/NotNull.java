package io.github.dunwu.javacore.annotation.custom;

/**
 * 标记注解 - 字段不能为空
 * @author Zhang Peng
 * @date 2019-03-31
 * @see NotNullUtil
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

}
