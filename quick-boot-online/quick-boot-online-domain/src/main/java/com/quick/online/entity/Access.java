package com.quick.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.common.aspect.annotation.Dict;
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
@Schema(name = "表单管理")
public class Access implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

    @Dict(dictCode = "access_debug")
    @Schema(description = "是否为 DEBUG: 0-否，1-是。")
    private int debug;

    @TableField(value = "`schema`")
    @Schema(description = "数据库名/模式")
    private String schema;

    @TableField(value = "`name`")
    @Schema(description = "表名")
    private String name;

    @Schema(description = "表别名")
    private String alias;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Schema(description = "描述")
    private String detail;

    @Schema(description = "删除字段名")
    @TableField(value = "deletedKey")
    private String deletedKey;

    @Schema(description = "删除字段值")
    @TableField(value = "deletedValue")
    private String deletedValue;

    @Schema(description = "未删除字段值")
    @TableField(value = "notDeletedValue")
    private String notDeletedValue;

}
