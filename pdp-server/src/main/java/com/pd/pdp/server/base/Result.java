package com.pd.pdp.server.base;

import lombok.Data;

/**
 * @Author pdp
 * @Description 接口统一返回类
 **/

@Data
public class Result <T> {
    private String code;

    private String msg;

    private T data;

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }
}