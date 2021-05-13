/**
 * The Apache License 2.0 Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2016/10/27.
 */
public class RegexUtil {

    /**
     * 最实用的正则
     */
    private static final String REGEX_ONLY_CHINESE_CHAR = "^[\u4e00-\u9fa5]+$";

    private static final String REGEX_ID_CARD_15 =
        "^((1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\\d{4})((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229)(\\d{3})$";

    private static final String REGEX_ID_CARD_18 =
        "^((1[1-5]|2[1-3]|3[1-7]|4[1-3]|5[0-4]|6[1-5])\\d{4})((\\d{4}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229)(\\d{3}(\\d|X))$";

    private static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    private static final String REGEX_EMAIL =
        "^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$";

    private static final String REGEX_URI = "^(ht|f)(tp|tps)\\://[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3})?(/\\S*)?$";

    private static final String REGEX_IPV4 =
        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final String REGEX_IPV6 =
        "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";

    private static final String REGEX_TIME = "^([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$";

    private static final String REGEX_DATE =
        "^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\2(?:29))$";

    private static final String REGEX_MOBILE = "^((\\+)?86\\s*)?((13[0-9])|(15([0-3]|[5-9]))|(18[0,2,5-9]))\\d{8}$";

    private static final String REGEX_PHONE = "^(010|02[0-9])(\\s|-)\\d{8}|(0[3-9]\\d{2})(\\s|-)\\d{7}$";

    /**
     * 特定数字
     */
    private static final String REGEX_NUM_ALL = "^[\\d]*$";

    private static final String REGEX_NUM_NONE = "^[\\D]*$";

    private static final String REGEX_N_NUMBER = "^\\d{%d}$";

    private static final String REGEX_MORE_THAN_N_NUMBER = "^\\d{%d,}$";

    private static final String REGEX_M_TO_N_NUMBER = "^\\d{%d,%d}$";

    /**
     * 特定字符
     */
    private static final String REGEX_CHAR_EN_ALL = "^[A-Za-z]+$";

    private static final String REGEX_CHAR_EN_UPPER_ALL = "^[A-Z]+$";

    private static final String REGEX_CHAR_EN_LOWER_ALL = "^[a-z]+$";

    private static final String REGEX_CHAR_UNI_WORD_ALL = "^\\w+$";

    private static final String REGEX_CHAR_UNI_WORD_NONE = "^\\W+$";

    /********************************************
     * 最常用的正则
     ********************************************/
    /**
     * 验证全是汉字字符 描述：校验字符串中只能有中文字符（不包括中文标点符号）。 匹配： 春眠不觉晓 不匹配：春眠不觉晓，
     */
    public static boolean isAllChineseChar(String content) {
        return checkMatches(RegexUtil.REGEX_ONLY_CHINESE_CHAR, content);
    }

    /**
     * 判断content是否匹配pattern的正则表达式
     */
    public static boolean checkMatches(String regex, String content) {
        return checkMatches(Pattern.compile(regex), content);
    }

    public static boolean checkMatches(Pattern p, String content) {
        Matcher m = p.matcher(content);
        return m.matches();
    }

    /**
     * 验证全是英文字母
     */
    public static boolean isAllEnglishChar(String content) {
        return checkMatches(RegexUtil.REGEX_CHAR_EN_ALL, content);
    }

    /**
     * 验证全是小写字母
     */
    public static boolean isAllLowerEnglishChar(String content) {
        return checkMatches(RegexUtil.REGEX_CHAR_EN_LOWER_ALL, content);
    }

    /**
     * 验证全是数字 正确格式：任意位数的数字
     */
    public static boolean isAllNumber(String content) {
        return checkMatches(RegexUtil.REGEX_NUM_ALL, content);
    }

    /**
     * 验证全是大写字母
     */
    public static boolean isAllUpperEnglishChar(String content) {
        return checkMatches(RegexUtil.REGEX_CHAR_EN_UPPER_ALL, content);
    }

    /**
     * 验证全是单词字符，即只能由数字、26个英文字母或者下划线组成
     */
    public static boolean isAllWordChar(String content) {
        return checkMatches(RegexUtil.REGEX_CHAR_UNI_WORD_ALL, content);
    }

    /**
     * 验证字符串至少是N位的数字
     */
    public static boolean isLeastNDigitNumber(String content, int n) {
        String format = String.format(RegexUtil.REGEX_MORE_THAN_N_NUMBER, n);
        return checkMatches(format, content);
    }

    /**
     * 验证字符串是M到N位之间的数字
     */
    public static boolean isMToNDigitNumber(String content, int m, int n) {
        String format = String.format(RegexUtil.REGEX_M_TO_N_NUMBER, m, n);
        return checkMatches(format, content);
    }

    /**
     * 验证是否为N位的数字
     */
    public static boolean isNDigitNumber(String content, int n) {
        String format = String.format(RegexUtil.REGEX_N_NUMBER, n);
        return checkMatches(format, content);
    }

    /**
     * 验证不含任何数字
     */
    public static boolean isNoneNumber(String content) {
        return checkMatches(RegexUtil.REGEX_NUM_NONE, content);
    }

    /**
     * 验证全部都不是单词字符，即不能由数字、26个英文字母或者下划线组成
     */
    public static boolean isNoneWordChar(String content) {
        return checkMatches(RegexUtil.REGEX_CHAR_UNI_WORD_NONE, content);
    }

    /********************************************
     * 特定数字
     ********************************************/

    /**
     * 校验时间。 描述：时、分、秒必须是有效数字，如果数值不是两位数，十位需要补零。 匹配：00:00:00 | 23:59:59 | 17:06:30 不匹配：17:6:30 | 24:16:30
     */
    public static boolean isValidTime(String content) {
        return checkMatches(RegexUtil.REGEX_TIME, content);
    }

    /**
     * 校验日期。 日期满足以下条件： 格式yyyy-MM-dd或yyyy-M-d 连字符可以没有或是“-”、“/”、“.”之一 闰年的二月可以有29日；而平年不可以。
     * 一、三、五、七、八、十、十二月为31日。四、六、九、十一月为30日。 匹配：2016/1/1 | 2016/01/01 | 20160101 | 2016-01-01 | 2016.01.01 | 2000-02-29
     * 不匹配：2001-02-29 | 2016/12/32 | 2016/6/31 | 2016/13/1 | 2016/0/1
     */
    public static boolean isValidateDate(String content) {
        return checkMatches(RegexUtil.REGEX_DATE, content);
    }

    /**
     * 校验有效邮箱 描述：不允许使用IP作为域名，如 : hello@154.145.68.12 '@'符号前的邮箱用户和'.'符号前的域名(domain)必须满足以下条件： 字符只能是英文字母、数字、下划线'_'、'.'、'-'
     * ； 首字符必须为字母或数字； '_'、'.'、'-' 不能连续出现。 域名的根域只能为字母，且至少为两个字符。 匹配：he_llo@worl.d.com | hel.l-o@wor-ld.museum |
     * h1ello@123.com 不匹配：hello@worl_d.com | he&llo@world.co1 | .hello@wor#.co.uk
     */
    public static boolean isValidateEmail(String content) {
        return checkMatches(RegexUtil.REGEX_EMAIL, content);
    }

    /**
     * 验证15位身份证号有效 描述：由15位数字组成。排列顺序从左至右依次为：六位数字地区码；六位数字出生日期；三位顺序号，其中15位男为单数，女为双数。 匹配：110001700101031
     * 不匹配：110001701501031
     */
    public static boolean isValidateIdCard15(String content) {
        return checkMatches(RegexUtil.REGEX_ID_CARD_15, content);
    }

    /**
     * 验证18位身份证号有效 描述：由十七位数字本体码和一位数字校验码组成。排列顺序从左至右依次为：六位数字地区码；八位数字出生日期；三位数字顺序码和一位数字校验码（也可能是X）。 匹配：110001199001010310 |
     * 11000019900101015X 不匹配：990000199001010310 | 110001199013010310
     */
    public static boolean isValidateIdCard18(String content) {
        return checkMatches(RegexUtil.REGEX_ID_CARD_18, content);
    }

    /********************************************
     * 特定字符
     ********************************************/

    /**
     * 验证IPv4有效 描述：IP地址是一个32位的二进制数，通常被分割为4个“8位二进制数”（也就是4个字节）。 IP地址通常用“点分十进制”表示成（a.b.c.d）的形式，其中，a,b,c,d都是0~255之间的十进制整数。
     * 匹配：0.0.0.0 | 255.255.255.255 | 127.0.0.1 不匹配：10.10.10 | 10.10.10.256
     */
    public static boolean isValidateIpv4(String content) {
        return checkMatches(RegexUtil.REGEX_IPV4, content);
    }

    /**
     * 验证IPv6有效 描述：IPv6的128位地址通常写成8组，每组为四个十六进制数的形式。 IPv6地址可以表示为以下形式： IPv6 地址 零压缩 IPv6 地址(section 2.2 of rfc5952)
     * 带有本地链接区域索引的 IPv6 地址 (section 11 of rfc4007) 嵌入IPv4的 IPv6 地址(section 2 of rfc6052 映射IPv4的 IPv6 地址 (section 2.1 of
     * rfc2765) 翻译IPv4的 IPv6 地址 (section 2.1 of rfc2765) 匹配：1:2:3:4:5:6:7:8 | 1:: | 1::8 | 1::6:7:8 | 1::5:6:7:8 |
     * 1::4:5:6:7:8 | 1::3:4:5:6:7:8 | ::2:3:4:5:6:7:8 | 1:2:3:4:5:6:7:: | 1:2:3:4:5:6::8 | 1:2:3:4:5::8 | 1:2:3:4::8 |
     * 1:2:3::8 | 1:2::8 | 1::8 | ::8 | fe80::7:8%1 | ::255.255.255.255 | 2001:db8:3:4::192.0.2.33 | 64:ff9b::192.0.2.33
     * 不匹配：1.2.3.4.5.6.7.8 | 1::2::3
     */
    public static boolean isValidateIpv6(String content) {
        return checkMatches(RegexUtil.REGEX_IPV6, content);
    }

    /**
     * 校验中国手机号码 描述：中国手机号码正确格式：11位数字。 移动有16个号段：134、135、136、137、138、139、147、150、151、152、157、158、159、182、187、188。其中147、157、188是3G号段，其他都是2G号段。
     * 联通有7种号段：130、131、132、155、156、185、186。其中186是3G（WCDMA）号段，其余为2G号段。 电信有4个号段：133、153、180、189。其中189是3G号段（CDMA2000），133号段主要用作无线网卡号。
     * 总结：13开头手机号0-9；15开头手机号0-3、5-9；18开头手机号0、2、5-9。 此外，中国在国际上的区号为86，所以手机号开头有+86、86也是合法的。 匹配：+86 18012345678 | 86
     * 18012345678 | 15812345678 不匹配：15412345678 | 12912345678 | 180123456789
     */
    public static boolean isValidateMobile(String content) {
        return checkMatches(RegexUtil.REGEX_MOBILE, content);
    }

    /**
     * 校验中国固话号码（大陆地区） 描述：固话号码，必须加区号（以0开头）。 3位有效区号：010、020~029，固话位数为8位 4位有效区号：03xx开头到09xx，固话位数为7
     */
    public static boolean isValidatePhone(String content) {
        return checkMatches(RegexUtil.REGEX_PHONE, content);
    }

    /**
     * 校验URL。 描述：支持http、https、ftp、ftps。 匹配：http://google.com/help/me | http://www.google.com/help/me/ |
     * https://www.google.com/help.asp | ftp://www.google.com | ftps://google.org 不匹配：http://un/www.google.com/index.asp
     */
    public static boolean isValidateUrl(String content) {
        return checkMatches(RegexUtil.REGEX_URI, content);
    }

    /**
     * 验证密码有效
     */
    public static boolean isValidateUsername(String content) {
        return checkMatches(RegexUtil.REGEX_USERNAME, content);
    }

    public static void main(String[] args) {
        checkMatches(REGEX_IPV4, "127.0.0.1");
        checkMatches(REGEX_IPV4, "0.0.0.0");
        checkMatches(REGEX_IPV4, "255.255.255.255");
    }

}
