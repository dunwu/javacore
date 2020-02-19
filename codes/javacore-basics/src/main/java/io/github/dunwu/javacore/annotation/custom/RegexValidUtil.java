package io.github.dunwu.javacore.annotation.custom;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-31
 */
public class RegexValidUtil {

    public static boolean check(Object obj) throws Exception {
        boolean result = true;
        StringBuilder sb = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断成员是否被 @RegexValid 注解所修饰
            if (field.isAnnotationPresent(RegexValid.class)) {
                RegexValid valid = field.getAnnotation(RegexValid.class);

                // 如果 value 为空字符串，说明没有注入自定义正则表达式，改用 policy 属性
                String value = valid.value();
                if ("".equals(value)) {
                    RegexValid.Policy policy = valid.policy();
                    value = policy.getPolicy();
                }

                // 通过设置 setAccessible(true) 来访问私有成员
                field.setAccessible(true);
                Object fieldObj = null;
                try {
                    fieldObj = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (fieldObj == null) {
                    sb.append("\n")
                        .append(String.format("%s 类中的 %s 字段不能为空！", obj.getClass().getName(), field.getName()));
                    result = false;
                } else {
                    if (fieldObj instanceof String) {
                        String text = (String) fieldObj;
                        Pattern p = Pattern.compile(value);
                        Matcher m = p.matcher(text);
                        result = m.matches();
                        if (!result) {
                            sb.append("\n").append(String.format("%s 不是合法的 %s ！", text, field.getName()));
                        }
                    } else {
                        sb.append("\n").append(String.format("%s 类中的 %s 字段不是字符串类型，不能使用此注解校验！", obj.getClass().getName(),
                            field.getName()));
                        result = false;
                    }
                }
            }
        }

        if (sb.length() > 0) {
            throw new Exception(sb.toString());
        }
        return result;
    }

}
