package com.pd.pdp.base;

/**
 * @Author pdp
 * @Description 响应码枚举，参考HTTP状态码的语义
 * @Date 2020-10-14 9:47 上午
 **/

public enum ResultCode {
    SUCCESS("200"),//成功
    FAIL("400"),//失败
    UNAUTHORIZED("401"),//未认证（签名错误）
    NOT_FOUND("404"),//接口不存在
    INTERNAL_SERVER_ERROR("500"),//服务器内部错误
    ERROR("9999");

    private final String code;

    ResultCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
