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
@Schema(name = "保存-字典数据信息")
public class SysDictDataSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "字典ID")
    private String dictId;

    @Schema(description = "字典文本")
    private String dictText;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "状态")
    private String status;
}
