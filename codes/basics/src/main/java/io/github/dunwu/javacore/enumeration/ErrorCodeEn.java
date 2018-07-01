/**
 * The Apache License 2.0
 * Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.enumeration;

/**
 * 错误码常量集（通用方式）
 *
 * @author Zhang Peng
 * @date 2016/11/24.
 * @see org.zp.javase.enumeration.ErrorCodeEn2
 * @see org.zp.javase.enumeration.AddMethod2EnumDemo
 */
public enum ErrorCodeEn {
    OK(0, "成功"),
    ERROR_A(100, "错误A"),
    ERROR_B(200, "错误B");

    ErrorCodeEn(int number, String description) {
        this.code = number;
        this.description = description;
    }

    private int code;
    private String description;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getAll() {
        String result = "[";
        for (ErrorCodeEn code : ErrorCodeEn.values()) {
            result += code.getDescription() + ", ";
        }
        result += "]";
        return result;
    }

    public static void main(String[] args) {
        System.out.println("getAll: " + ErrorCodeEn.getAll());
        for (ErrorCodeEn s : ErrorCodeEn.values()) {
            System.out.println("name: " + s.getDescription() + ", code: " + s.getCode());
        }
    }
}
