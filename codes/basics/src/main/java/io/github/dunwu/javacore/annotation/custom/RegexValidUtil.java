package io.github.dunwu.javacore.annotation.custom;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zhang Peng
 * @date 2019-03-31
 */
public class RegexValidUtil {
    public static boolean check(Object obj) throws Exception {
        boolean result = true;
        StringBuilder sb = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RegexValid.class)) {
                RegexValid valid = field.getAnnotation(RegexValid.class);
                String regex = valid.value();
                if ("".equals(regex)) {
                    RegexValid.Policy policy = valid.regex();
                    regex = policy.getRegex();
                }

                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value == null) {
                    sb.append("\n")
                        .append(String.format("%s 类中的 %s 字段不能为空！", obj.getClass().getName(), field.getName()));
                    result = false;
                } else {
                    if (value instanceof String) {
                        String text = (String) value;
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(text);
                        result = m.matches();
                        if (!result) {
                            sb.append("\n").append(String.format("%s 不是合法的 %s ！", text, field.getName()));
                        }
                    } else {
                        sb.append("\n").append(
                            String.format("%s 类中的 %s 字段不是字符串类型，不能使用此注解校验！", obj.getClass().getName(), field.getName()));
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
