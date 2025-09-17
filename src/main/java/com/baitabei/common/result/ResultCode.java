package com.baitabei.common.result;

/**
 * 响应状态码枚举
 * 
 * @author MiniMax Agent
 */
public enum ResultCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "数据冲突"),
    VALIDATE_FAILED(422, "参数校验失败"),

    // 服务器错误
    INTERNAL_SERVER_ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // 用户相关错误码 (1000-1999)
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_DISABLED(1002, "用户已被禁用"),
    PASSWORD_ERROR(1003, "密码错误"),
    OLD_PASSWORD_ERROR(1004, "旧密码错误"),
    TOKEN_INVALID(1005, "token无效"),
    TOKEN_EXPIRED(1006, "token已过期"),
    PERMISSION_DENIED(1007, "权限不足"),
    USERNAME_ALREADY_EXISTS(1008, "用户名已存在"),
    EMAIL_ALREADY_EXISTS(1009, "邮箱已被使用"),
    PHONE_ALREADY_EXISTS(1010, "手机号已被使用"),
    CONFIRM_PASSWORD_NOT_MATCH(1011, "确认密码不匹配"),
    VERIFICATION_CODE_ERROR(1012, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(1013, "验证码已过期"),
    LOGIN_FAILED(1014, "登录失败"),

    // 项目相关错误码 (2000-2999)
    PROJECT_NOT_FOUND(2001, "项目不存在"),
    PROJECT_STATUS_ERROR(2002, "项目状态错误"),
    PROJECT_ALREADY_SUBMITTED(2003, "项目已提交"),
    PROJECT_NOT_OWNER(2004, "不是项目所有者"),
    PROJECT_CODE_ALREADY_EXISTS(2005, "项目编号已存在"),
    PROJECT_PROJECT_ALREADY_EXISTS(2005, "作品名称不可重复"),

    // 赛道相关错误码 (3000-3999)
    TRACK_NOT_FOUND(3001, "赛道不存在"),
    TRACK_CLOSED(3002, "赛道已关闭"),
    TRACK_NOT_STARTED(3003, "赛道尚未开始"),
    TRACK_ENDED(3004, "赛道已结束"),
    
    // 评审相关错误码 (4000-4999)
    EVALUATION_NOT_FOUND(4001, "评分记录不存在"),
    EVALUATION_ALREADY_SUBMITTED(4002, "评分已提交"),
    EVALUATION_PERMISSION_DENIED(4003, "无评审权限"),
    EVALUATION_SCORE_INVALID(4004, "评分数值无效"),
    
    // 文件相关错误码 (5000-5999)
    FILE_UPLOAD_ERROR(5001, "文件上传失败"),
    FILE_TYPE_NOT_ALLOWED(5002, "文件类型不允许"),
    FILE_SIZE_EXCEEDED(5003, "文件大小超出限制"),
    FILE_NOT_FOUND(5004, "文件不存在"),
    FILE_DELETE_ERROR(5005, "文件删除失败"),
    
    // 消息通知相关错误码 (6000-6999)
    SEND_EMAIL_ERROR(6001, "邮件发送失败"),
    SEND_SMS_ERROR(6002, "短信发送失败"),
    EMAIL_TEMPLATE_NOT_FOUND(6003, "邮件模板不存在"),
    SMS_TEMPLATE_NOT_FOUND(6004, "短信模板不存在"),
    
    // 角色权限相关错误码 (7000-7999)
    ROLE_NOT_FOUND(7001, "角色不存在"),
    ROLE_ALREADY_EXISTS(7002, "角色已存在"),
    PERMISSION_NOT_FOUND(7003, "权限不存在"),
    ROLE_HAS_USERS(7004, "角色下还有用户，无法删除");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}