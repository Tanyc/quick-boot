package com.quick.modules.system.entity;

import com.quick.common.aspect.annotation.Dict;
import com.quick.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "字典信息")
public class SysDict extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典编码")
    private String dictCode;

    @Dict(dictCode = "sys_dict_status")
    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "删除状态")
    private Integer delFlag;

}
