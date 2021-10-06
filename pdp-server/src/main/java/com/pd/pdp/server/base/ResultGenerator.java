package com.pd.pdp.server.base;

/**
 * @Author pdp
 * @Description 响应结果生成工具
 **/

public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "成功";

    public static Result getSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> getSuccessResult(T data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMsg(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result getFailResult(String msg) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMsg(msg);
    }

    public static Result getErrorResult(String msg) {
        return new Result()
                .setCode(ResultCode.ERROR)
                .setMsg(msg);
    }
}
