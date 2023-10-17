package com.quick.modules.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.modules.online.entity.Access;
import com.quick.modules.online.entity.Request;
import com.quick.modules.online.entity.SysAccessSchema;
import com.quick.modules.online.entity.SysTableColumn;
import com.quick.modules.online.mapper.AccessMapper;
import com.quick.modules.online.mapper.RequestMapper;
import com.quick.modules.online.mapper.SysAccessSchemaMapper;
import com.quick.modules.online.mapper.SysTableColumnMapper;
import com.quick.modules.online.service.ISysAccessSchemaService;
import com.quick.modules.util.AMISGeneratorUtils;
import com.quick.modules.util.APIJSONGeneratorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysAccessSchemaServiceImpl extends ServiceImpl<SysAccessSchemaMapper, SysAccessSchema> implements ISysAccessSchemaService {

    private  final AccessMapper accessMapper;

    private final RequestMapper requestMapper;

    private final SysTableColumnMapper sysTableColumnMapper;

    @Override
    public SysAccessSchema getSchema(String accessId) {
        SysAccessSchema sysAccessSchema = this.getOne(new LambdaQueryWrapper<SysAccessSchema>().eq(SysAccessSchema::getAccessId, accessId));
        if(sysAccessSchema==null){
            // 初始化进去
            Access access = accessMapper.selectById(accessId);
            // 表字段信息
            List<SysTableColumn> fieldList = sysTableColumnMapper.selectList(new LambdaQueryWrapper<SysTableColumn>()
                    .eq(SysTableColumn::getAccessId,accessId));
            // 1.生成 amis crud json 配置
            String alias = StringUtils.hasText(access.getAlias())?access.getAlias():access.getName();
            String schema = AMISGeneratorUtils.generateAMISCrudSchema(alias,fieldList);
            // 2.保存 schema
            sysAccessSchema = SysAccessSchema.builder()
                    .accessId(accessId)
                    .schema(schema)
                    .build();
            this.save(sysAccessSchema);

            // 初始化 crud 接口
            // 删除
            requestMapper.insert(APIJSONGeneratorUtils.deleteById(alias));
            requestMapper.insert(APIJSONGeneratorUtils.batchDeleteById(alias));
            // 更新
            requestMapper.insert(APIJSONGeneratorUtils.updateById(alias,fieldList));
            // 新增
            requestMapper.insert(APIJSONGeneratorUtils.save(alias,fieldList));

        }
        return sysAccessSchema;
    }
}
