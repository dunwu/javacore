package io.github.dunwu.javacore.oop;

/**
 * @author Zhang Peng
 * @date 2019-03-16
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Programmer programmer = new Programmer();
        programmer.setName("zp");
        System.out.println("I am a programmer");
        System.out.println("My name is " + programmer.getName());
        System.out.println("My core skill is " + Programmer.CORE_SKILL);
    }
}
