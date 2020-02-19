package io.github.dunwu.javacore.oop;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-16
 */
public class Programmer {

    public static final String CORE_SKILL;

    static {
        CORE_SKILL = "programming";
    }

    private String name;

    public Programmer() {
        name = "unknown";
    }

    public static void main(String[] args) {
        Programmer programmer = new Programmer();
        programmer.setName("zp");
        System.out.println("I am a programmer");
        System.out.println("My name is " + programmer.getName());
        System.out.println("My core skill is " + Programmer.CORE_SKILL);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
