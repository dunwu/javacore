/**
 * The Apache License 2.0
 * Copyright (c) 2016 Zhang Peng
 */
package io.github.dunwu.javacore.enumeration;

/**
 * 错误码常量集
 * 展示枚举可以实现接口
 *
 * @author Zhang Peng
 * @date 2016/11/24.
 * @see org.zp.javase.enumeration.ErrorCodeEn
 */
public enum ErrorCodeEn2 implements INumberEnum {
    OK(0, "成功"),
    ERROR_A(100, "错误A"),
    ERROR_B(200, "错误B");

    ErrorCodeEn2(int number, String description) {
        this.code = number;
        this.description = description;
    }

    private int code;
    private String description;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
