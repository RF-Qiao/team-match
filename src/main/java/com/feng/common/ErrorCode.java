package com.feng.common;

/**
 * 200 OK
 * 201 返回结果为空
 * 301 请求参数错误
 * 302 请求参数为空
 * 401 未登录
 * 402 没有权限访问
 * 500 系统内部异常
 */
public enum ErrorCode {
    SUCCESS(200, "ok", ""),
    PARAM_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(400001, "请求为空", ""),
    NOT_LOGIN(401000, "未登录", ""),
    NOT_AUTH(40200, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode()
    {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }
}
