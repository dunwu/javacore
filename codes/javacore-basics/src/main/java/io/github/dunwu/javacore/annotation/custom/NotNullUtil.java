package io.github.dunwu.javacore.annotation.custom;

import java.lang.reflect.Field;

/**
 * Nullable 注解处理器
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see NotNull
 * @since 2019-03-31
 */
public class NotNullUtil {

    public static void check(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(NotNull.class)) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value == null) {
                    String msg = String.format("%s 类中的 %s 字段不能为空！", obj.getClass().getName(), field.getName());
                    throw new NullPointerException(msg);
                }
            }
        }
    }

}
