package com.swx.common.pojo;

public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(2000, "成功"),
    /* 参数错误 */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    /* 用户错误 2001-2999*/
    USER_NOTLOGGED_IN(2001, "用户未登录"),
    USER_LOGIN_ERROR(2002, "用户名或密码错误"),
    USER_DISABLE(2003, "账户已被禁用，请联系管理员!"),
    LOGIN_AUTH_FAIL(2004, "无认证信息，请重新登陆!"),
    TOKEN_INVALID(2005, "无效Token，请重新登陆!"),

    TOKEN_UNSUPPORTED(2006, "非法Token"),
    SYSTEM_ERROR(1000, "系统异常，请稍后重试"),
    NO_PERMISSION(1001, "无操作权限，请联系管理员!");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }
    public String message() {
        return this.message;
    }
}
