package io.github.dunwu.javacore.annotation.custom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * annotation.custom 包自定义注解示例单元测试
 * <p>
 * 覆盖元注解声明、注解属性默认值、{@link NotNullUtil} 与 {@link RegexValidUtil} 两个注解处理器的
 * 正常/异常路径，以及使用限制场景
 */
@DisplayName("自定义注解示例测试")
public class CustomAnnotationTest {

    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    // ==================== 元注解与注解属性 ====================

    @Test
    @DisplayName("NotNull 元注解：目标为 FIELD、保留到 RUNTIME，才能被反射读取")
    void testNotNullMetaAnnotation() {
        assertThat(NotNull.class.getAnnotation(Target.class).value()).containsExactly(ElementType.FIELD);
        assertThat(NotNull.class.getAnnotation(Retention.class).value()).isEqualTo(RetentionPolicy.RUNTIME);
        assertThat(NotNull.class.isAnnotationPresent(Documented.class)).isTrue();
    }

    @Test
    @DisplayName("RegexValid 元注解：目标含 FIELD 与 PARAMETER，保留到 RUNTIME")
    void testRegexValidMetaAnnotation() {
        assertThat(RegexValid.class.getAnnotation(Target.class).value())
            .containsExactlyInAnyOrder(ElementType.FIELD, ElementType.PARAMETER);
        assertThat(RegexValid.class.getAnnotation(Retention.class).value()).isEqualTo(RetentionPolicy.RUNTIME);
    }

    @Test
    @DisplayName("RegexValid 注解属性默认值：value 为空串、policy 为 EMPTY")
    void testRegexValidDefaultValues() throws Exception {
        assertThat(RegexValid.class.getMethod("value").getDefaultValue()).isEqualTo("");
        assertThat(RegexValid.class.getMethod("policy").getDefaultValue()).isEqualTo(RegexValid.Policy.EMPTY);
    }

    @Test
    @DisplayName("RegexValid.Policy：EMPTY 无正则，DATE/MAIL 提供内置正则")
    void testRegexValidPolicy() {
        assertThat(RegexValid.Policy.EMPTY.getPolicy()).isNull();
        assertThat(RegexValid.Policy.DATE.getPolicy()).isNotBlank();
        assertThat(RegexValid.Policy.MAIL.getPolicy()).isNotBlank();
        // DATE 策略能识别闰年：2020-02-29 合法，2019-02-29 非法
        assertThat("2020-02-29").matches(RegexValid.Policy.DATE.getPolicy());
        assertThat("2019-02-29").doesNotMatch(RegexValid.Policy.DATE.getPolicy());
    }

    // ==================== NotNullUtil 处理器 ====================

    @Test
    @DisplayName("NotNullUtil：被 @NotNull 修饰的字段为 null 时抛 NullPointerException 并带字段名")
    void testNotNullUtilRejectsNull() {
        assertThatThrownBy(() -> NotNullUtil.check(new NotNullBean(null)))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("id 字段不能为空！");
    }

    @Test
    @DisplayName("NotNullUtil：被 @NotNull 修饰的字段非 null 时校验通过")
    void testNotNullUtilAcceptsNonNull() {
        assertThatCode(() -> NotNullUtil.check(new NotNullBean(1))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("NotNullUtil 使用限制：未标注 @NotNull 的字段即使为 null 也不会被校验")
    void testNotNullUtilIgnoresUnannotatedField() {
        // name 字段没有 @NotNull，因此为 null 也通过——注解只对显式标记的字段生效
        assertThatCode(() -> NotNullUtil.check(new NotNullBean(1, null))).doesNotThrowAnyException();
    }

    // ==================== RegexValidUtil 处理器 ====================

    @Test
    @DisplayName("RegexValidUtil：日期、邮箱、手机号全部合法时返回 true")
    void testRegexValidUtilAllValid() throws Exception {
        assertThat(RegexValidUtil.check(new RegexBean("Tom", "1990-01-31", "xxx@163.com", "18612341234"))).isTrue();
    }

    @Test
    @DisplayName("RegexValidUtil：非闰年的 2 月 29 日不匹配 DATE 策略，抛异常并指出字段名")
    void testRegexValidUtilRejectsInvalidDate() {
        assertThatThrownBy(() -> RegexValidUtil.check(new RegexBean("Jack", "2019-02-29", "xxx@163.com", "18612341234")))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("2019-02-29 不是合法的 date ！");
    }

    @Test
    @DisplayName("RegexValidUtil：value 为空串时回退到 policy 指定的正则")
    void testRegexValidUtilFallsBackToPolicy() {
        // mail 缺少 @ 符号，MAIL 策略校验不通过
        assertThatThrownBy(() -> RegexValidUtil.check(new RegexBean("Jack", "1990-01-31", "sadhgs", "18612341234")))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("sadhgs 不是合法的 mail ！");
    }

    @Test
    @DisplayName("RegexValidUtil：自定义 value 正则优先于 policy 生效")
    void testRegexValidUtilUsesCustomValue() {
        // phone 字段用自定义正则 ^\d{11}$ 校验，含字母即不通过
        assertThatThrownBy(() -> RegexValidUtil.check(new RegexBean("Jack", "1990-01-31", "xxx@163.com", "183xxxxxxxx")))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("183xxxxxxxx 不是合法的 phone ！");
    }

    @Test
    @DisplayName("RegexValidUtil 使用限制：被标注字段为 null 时按「不能为空」处理，不做正则匹配")
    void testRegexValidUtilRejectsNullField() {
        assertThatThrownBy(() -> RegexValidUtil.check(new RegexBean("Jack", "1990-01-31", null, "18612341234")))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("mail 字段不能为空！");
    }

    @Test
    @DisplayName("RegexValidUtil 使用限制：被标注字段不是 String 类型时无法校验")
    void testRegexValidUtilRejectsNonStringField() {
        assertThatThrownBy(() -> RegexValidUtil.check(new NonStringBean()))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("notString 字段不是字符串类型，不能使用此注解校验！");
    }

    @Test
    @DisplayName("RegexValidUtil 使用限制：不指定 value 且 policy 保持默认 EMPTY 时，正则为 null 会抛 NullPointerException")
    void testRegexValidUtilEmptyPolicyLimitation() {
        // Policy.EMPTY 的正则为 null，处理器未做兜底，Pattern.compile(null) 直接抛 NPE。
        // 因此使用 @RegexValid 时必须显式指定 value 或 policy，不能只写裸注解
        assertThatThrownBy(() -> RegexValidUtil.check(new EmptyPolicyBean()))
            .isInstanceOf(NullPointerException.class);
    }

    // ==================== 示例入口 ====================

    @Test
    @DisplayName("NotNullDemo：同一校验器对 null 与非 null 字段分别给出失败与通过结论")
    void testNotNullDemo() {
        String output = captureOutput(NotNullDemo::demo);
        assertThat(output).isEqualTo("id 为 null：校验失败 -> "
            + "io.github.dunwu.javacore.annotation.custom.NotNullDemo$MyBean 类中的 id 字段不能为空！\n"
            + "id 不为 null：校验通过\n");
    }

    @Test
    @DisplayName("RegexValidDemo：合法数据通过校验，非法数据汇总输出全部失败原因")
    void testRegexValidDemo() {
        String output = captureOutput(RegexValidDemo::demo);
        assertThat(output).isEqualTo("User{name='Tom', date='1990-01-31', mail='xxx@163.com', phone='18612341234'} 正则校验通过\n"
            + "正则校验失败，原因:\n"
            + "2019-02-29 不是合法的 date ！\n"
            + "sadhgs 不是合法的 mail ！\n"
            + "183xxxxxxxx 不是合法的 phone ！\n");
    }

    // ==================== 测试用 Bean ====================

    /**
     * 带 {@code @NotNull} 字段的 Bean，用于验证 {@link NotNullUtil}
     */
    static class NotNullBean {

        @NotNull
        private Integer id;

        /**
         * 未标注解，用于验证「注解只对显式标记的字段生效」
         */
        private String name;

        NotNullBean(Integer id) {
            this(id, "jack");
        }

        NotNullBean(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

    }

    /**
     * 带多个 {@code @RegexValid} 字段的 Bean，用于验证 {@link RegexValidUtil}
     */
    static class RegexBean {

        /**
         * 未标注解，不参与校验
         */
        private String name;

        @RegexValid(policy = RegexValid.Policy.DATE)
        private String date;

        @RegexValid(policy = RegexValid.Policy.MAIL)
        private String mail;

        @RegexValid("^\\d{11}$")
        private String phone;

        RegexBean(String name, String date, String mail, String phone) {
            this.name = name;
            this.date = date;
            this.mail = mail;
            this.phone = phone;
        }

    }

    /**
     * 被标注字段不是 String 类型，用于验证处理器的类型限制
     */
    static class NonStringBean {

        @RegexValid(policy = RegexValid.Policy.MAIL)
        private Integer notString = 123;

    }

    /**
     * 只写裸注解 {@code @RegexValid}，policy 为默认 EMPTY，用于验证其正则为 null 的限制
     */
    static class EmptyPolicyBean {

        @RegexValid
        private String text = "abc";

    }

}
