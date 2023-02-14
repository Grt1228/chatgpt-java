package com.unfbx.chatgpt.exception;

/**
 * 描述： 错误
 *
 * @author https:www.unfbx.com
 * @date 2023-02-11
 */
public enum CommonError implements IError {
    SYS_ERROR(500, "系统繁忙"),
    PARAM_ERROR(501, "参数异常"),
    RETRY_ERROR(502, "请求异常，请重试~"),
    MAX_TOKENS_ERROR(503, "max_tokens最大值4096"),
    ;


    private int code;
    private String msg;

    CommonError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String msg() {
        return this.msg;
    }

    @Override
    public int code() {
        return this.code;
    }
}
