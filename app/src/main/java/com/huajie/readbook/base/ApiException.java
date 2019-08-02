package com.huajie.readbook.base;

/**
 *描述：异常处理基类
 *作者：Created by zhuzhen
 */

public class ApiException extends RuntimeException {
    private String errorCode;

    public ApiException(String code, String msg) {
        super(msg);
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
