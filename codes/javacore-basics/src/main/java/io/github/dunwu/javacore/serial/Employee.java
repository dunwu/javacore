package io.github.dunwu.javacore.serial;

public class Employee implements java.io.Serializable {

    private static final long serialVersionUID = 5780029576777559095L;

    public String name;

    public String address;

    public transient int SSN;

    public int number;

    public int demo;

}
