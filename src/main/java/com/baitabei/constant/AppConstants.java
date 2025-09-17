package com.baitabei.constant;

/**
 * 应用常量
 * 
 * @author MiniMax Agent
 */
public class AppConstants {

    private AppConstants() {
        // 禁止实例化
    }

    // JWT相关常量
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_NAME = "Authorization";
    public static final long JWT_EXPIRATION_TIME = 86400000L; // 24小时
    public static final long JWT_REFRESH_EXPIRATION_TIME = 604800000L; // 7天

    // Redis相关常量
    public static final String REDIS_USER_PREFIX = "user:";
    public static final String REDIS_TOKEN_PREFIX = "token:";
    public static final String REDIS_VERIFICATION_CODE_PREFIX = "verification:";
    public static final int VERIFICATION_CODE_EXPIRE_TIME = 300; // 5分钟

    // 默认分页参数
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 100;
    public static final Integer DEFAULT_PAGE_NUM = 1;

    // 角色常量
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_EXPERT = "EXPERT";
    public static final String ROLE_ORGANIZER = "ORGANIZER";

    // 文件上传常量
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024L; // 10MB
    public static final String[] ALLOWED_IMAGE_TYPES = {".jpg", ".jpeg", ".png", ".gif"};
    public static final String[] ALLOWED_DOCUMENT_TYPES = {".pdf", ".doc", ".docx", ".ppt", ".pptx"};
    public static final String[] ALLOWED_VIDEO_TYPES = {".mp4", ".avi", ".mov", ".wmv"};

    // 消息常量
    public static final String SUCCESS_MESSAGE = "操作成功";
    public static final String FAILURE_MESSAGE = "操作失败";
    public static final String LOGIN_SUCCESS_MESSAGE = "登录成功";
    public static final String LOGIN_FAILURE_MESSAGE = "登录失败";
    public static final String REGISTER_SUCCESS_MESSAGE = "注册成功";
    public static final String REGISTER_FAILURE_MESSAGE = "注册失败";

    // 评审轮次
    public static final Integer FIRST_ROUND = 1;
    public static final Integer SECOND_ROUND = 2;
    public static final Integer FINAL_ROUND = 3;

    // 文件类型
    public static final String FILE_TYPE_DOCUMENT = "document";
    public static final String FILE_TYPE_IMAGE = "image";
    public static final String FILE_TYPE_VIDEO = "video";
    public static final String FILE_TYPE_OTHER = "other";

    // 赛道状态
    public static final Integer TRACK_STATUS_CLOSED = 0;
    public static final Integer TRACK_STATUS_OPEN = 1;

    // 通用状态
    public static final Integer STATUS_DISABLED = 0;
    public static final Integer STATUS_ENABLED = 1;

    // 性别
    public static final Integer GENDER_UNKNOWN = 0;
    public static final Integer GENDER_MALE = 1;
    public static final Integer GENDER_FEMALE = 2;

    // 评分满分
    public static final Integer MAX_SCORE = 100;
    public static final Integer MIN_SCORE = 0;
}
