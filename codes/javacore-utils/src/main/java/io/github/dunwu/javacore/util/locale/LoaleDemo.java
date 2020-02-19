/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.util.locale;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 通过ResourceBundle加载定义好的多语言文件来实现本地化
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/11/25.
 */
public class LoaleDemo {

    public static void main(String[] args) {
        // 根据语言+地区编码初始化
        ResourceBundle rbUS = ResourceBundle.getBundle("locales.content", new Locale("en", "US"));
        // 根据Locale常量初始化
        ResourceBundle rbZhCN = ResourceBundle.getBundle("locales.content", Locale.SIMPLIFIED_CHINESE);
        // 获取本地系统默认的Locale初始化
        ResourceBundle rbDefault = ResourceBundle.getBundle("locales.content");
        // ResourceBundle rbDefault =ResourceBundle.getBundle("locales.content",
        // Locale.getDefault()); // 与上行代码等价

        System.out.println("us-US:" + rbUS.getString("helloWorld"));
        System.out.println("us-US:" + String.format(rbUS.getString("time"), "08:00"));
        System.out.println("zh-CN：" + rbZhCN.getString("helloWorld"));
        System.out.println("zh-CN：" + String.format(rbZhCN.getString("time"), "08:00"));
        System.out.println("default：" + rbDefault.getString("helloWorld"));
        System.out.println("default：" + String.format(rbDefault.getString("time"), "08:00"));
    }

    private void init() {
        // 初始化一个通用英语的locale.
        Locale locale1 = new Locale("en");
        // 初始化一个加拿大英语的locale.
        Locale locale2 = new Locale("en", "CA");
        // 初始化一个美式英语变种硅谷英语的locale
        Locale locale3 = new Locale("en", "US", "SiliconValley");
        // 根据Locale常量初始化一个简体中文
        Locale locale4 = Locale.SIMPLIFIED_CHINESE;
    }

}
