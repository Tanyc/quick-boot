package com.quick.system.dto;

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
@Schema(name = "保存-字典信息")
public class SysDictSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典编码")
    private String dictCode;

}
