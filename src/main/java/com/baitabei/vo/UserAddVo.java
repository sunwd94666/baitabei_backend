package com.baitabei.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "新增用户参数")
public class UserAddVo {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", required = true)
    private String username;

    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色ID", required = true)
    private Long roleId;

    @NotNull(message = "赛道ID不能为空")
    @Schema(description = "赛道ID", required = true)
    private Long trackId;

    /* getter / setter 略 */
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public Long getTrackId() { return trackId; }
    public void setTrackId(Long trackId) { this.trackId = trackId; }
}