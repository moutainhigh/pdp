package com.pd.pdp.server.enums;

/**
 * @Author pdp
 * @Description 系统常量类
 **/

public enum Constant {
    // 密码加盐
    PARENT_ID("加盐值", "JMX_"),
    // 系统ID
    SYS_ID("系统ID", "1"),
    // 初始密码
    PASSWORD_ID("初始密码", "123456");

    // 成员变量
    private String name;

    private String value;

    // 构造方法
    Constant(String name, String value) {
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getValue(String name) {
        for (Constant constant : Constant.values()) {
            if (constant.getName() == name) {
                return constant.getValue();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
