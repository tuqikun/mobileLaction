package com.cn.demo.exceptions;


public class RetryException extends RuntimeException {

    private int code;

    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public RetryException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
