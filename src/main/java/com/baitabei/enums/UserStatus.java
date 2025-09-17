package com.baitabei.enums;

/**
 * 用户状态枚举
 * 
 * @author MiniMax Agent
 */
public enum UserStatus {
    
    DISABLED(0, "禁用"),
    NORMAL(1, "正常"),
    PENDING(2, "待审核");

    private final Integer code;
    private final String description;

    UserStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static UserStatus getByCode(Integer code) {
        for (UserStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
