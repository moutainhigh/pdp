package com.pd.pdp.base;

/**
 * @Author pdp
 * @Description 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录
 * @Date 2020-10-14 9:48 上午
 **/

public class ServiceException extends RuntimeException {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
