package com.quick.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.common.aspect.annotation.Dict;
import com.quick.common.constant.CommonConstant;
import com.quick.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "租户信息")
public class SysTenant extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    @Schema(description = "租户名称")
    private String name;

    @Schema(description = "租户编码")
    private String code;

    @Schema(description = "租户域名")
    private String domain;

    @Schema(description = "网站名称")
    private String websiteName;

    @Schema(description = "登录背景图")
    private String background;

    @Schema(description = "页脚信息")
    private String footer;

    @Schema(description = "logo")
    private String logo;

    @Schema(description = "租户开始时间")
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime startTime;

    @Schema(description = "租户结束时间")
    @DateTimeFormat(pattern = CommonConstant.DATE_TIME_FORMAT)
    @JsonFormat(timezone = "GMT+8", pattern = CommonConstant.DATE_TIME_FORMAT)
    private LocalDateTime endTime;

    @Schema(description = "租户菜单ID")
    private LocalDateTime menu_id;

    @Dict(dictCode = "sys_tenant_status")
    @Schema(description = "状态")
    private String status;
}
