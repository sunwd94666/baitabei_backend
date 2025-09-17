package com.baitabei.enums;

/**
 * 项目状态枚举
 * 
 * @author MiniMax Agent
 */
public enum ProjectStatus {
    
    DRAFT(1, "草稿"),
    SUBMITTED(2, "已提交"),
    FIRST_REVIEW(3, "初审中"),
    FIRST_PASS(4, "初审通过"),
    FIRST_REJECT(5, "初审不通过"),
    SECOND_REVIEW(6, "复审中"),
    SECOND_PASS(7, "复审通过"),
    SECOND_REJECT(8, "复审不通过"),
    FINAL_REVIEW(9, "终审中"),
    AWARDED(10, "获奖"),
    ELIMINATED(11, "淘汰");

    private final Integer code;
    private final String description;

    ProjectStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProjectStatus getByCode(Integer code) {
        for (ProjectStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
