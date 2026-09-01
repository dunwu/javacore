package io.github.dunwu.javacore.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.TreeMap;

/**
 * 参数格式化工具
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2024-03-05
 */
public class ParamFormatUtil {

    public static String getFormatStr(Object object, long timestamp) {
        TreeMap<String, Object> paramMap = getSortedParamMap(object);
        if (MapUtil.isEmpty(paramMap)) {
            return StrUtil.EMPTY;
        }
        paramMap.put("timestamp", timestamp);
        return getFormatMap(paramMap);
    }

    @SuppressWarnings("all")
    public static TreeMap<String, Object> getSortedParamMap(Object object) {
        if (object == null) {
            return null;
        }
        TreeMap<String, Object> treeMap;
        if (object instanceof Map) {
            treeMap = new TreeMap<>((Map) object);
        } else {
            treeMap = new TreeMap<>(BeanUtil.beanToMap(object));
        }
        return treeMap;
    }

    public static String getFormatMap(Map<String, Object> params) {
        if (MapUtil.isEmpty(params)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (first) {
                sb.append(key).append("=").append(value);
            } else {
                sb.append("&").append(key).append("=").append(value);
            }
            first = false;
        }
        return sb.toString();
    }

}
