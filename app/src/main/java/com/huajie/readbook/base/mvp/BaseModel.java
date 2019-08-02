package com.huajie.readbook.base.mvp;

import java.io.Serializable;
/**
 *描述：mode基类
 *作者：Created by zhuzhen
 */
public class BaseModel<T> implements Serializable {
    private String msg;
    private String retcode;
    private T data;

    public BaseModel(String message, String retcode) {
        this.msg = message;
        this.retcode = retcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public T getData() {
        return data;
    }

    public void setData(T result) {
        this.data = result;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "retcode=" + retcode +
                ", msg='" + msg + '\'' +
                ", result=" + data +
                '}';
    }
}
