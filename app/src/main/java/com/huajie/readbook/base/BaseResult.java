package com.huajie.readbook.base;

/**
 * File descripition:   状态划分 基类
 *
 */

public class BaseResult {
    public String msg;
    public String retcode;

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public String getCode() {
        return retcode;
    }

    public void setCode(String code) {
        this.retcode = code;
    }
}
