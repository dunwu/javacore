package io.github.dunwu.javacore.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ResourceBundle 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2022-12-23
 */
public class ResourceBundleDemo {

    public static void main(String[] args) {
        // 根据语言+地区编码初始化
        ResourceBundle rbUS = ResourceBundle.getBundle("locales.content", new Locale("en", "US"));
        // 根据Locale常量初始化
        ResourceBundle rbZhCN = ResourceBundle.getBundle("locales.content", Locale.SIMPLIFIED_CHINESE);
        // 获取本地系统默认的Locale初始化
        ResourceBundle rbDefault = ResourceBundle.getBundle("locales.content");
        // ResourceBundle rbDefault =ResourceBundle.getBundle("locales.content", Locale.getDefault()); // 与上行代码等价

        System.out.println("en-US:" + rbUS.getString("helloWorld"));
        System.out.println("en-US:" + String.format(rbUS.getString("time"), "08:00"));
        System.out.println("zh-CN：" + rbZhCN.getString("helloWorld"));
        System.out.println("zh-CN：" + String.format(rbZhCN.getString("time"), "08:00"));
        System.out.println("default：" + rbDefault.getString("helloWorld"));
        System.out.println("default：" + String.format(rbDefault.getString("time"), "08:00"));
    }

}

// 输出：
// en-US:HelloWorld!
// en-US:The current time is 08:00.
// zh-CN：世界，你好！
// zh-CN：当前时间是08:00。
// default：世界，你好！
// default：当前时间是08:00。
