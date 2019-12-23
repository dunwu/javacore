package io.github.dunwu.javacore.concurrent.annotation;

import java.lang.annotation.*;

/**
 * 标记注解，用于标注示例为正确示例。与 {@link Error} 相对应
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-12-23
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Right {

}
