package com.quick.system.controller.api;

import cn.dev33.satoken.annotation.SaIgnore;
import com.quick.common.vo.Result;
import com.quick.system.service.ISysUserApiService;
import com.quick.system.entity.SysUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/user/api")
@RequiredArgsConstructor
@Tag(name = "用户信息API")
public class SysUserApiController {
    private final ISysUserApiService sysUserApiService;


    @GetMapping(value = "/findByUserId")
    @Operation(summary = "查询用户", description = "根据用户ID查询用户")
    public Result<SysUser> findByUserId(@RequestParam(value = "userId") Long userId) {
        return Result.success(sysUserApiService.findByUserId(userId));
    }

    @SaIgnore
    @GetMapping(value = "/findByUsername")
    @Operation(summary = "查询用户", description = "根据用户账号查询用户")
    public Result<SysUser> findByUsername(@RequestParam(value = "username") String username) {
        return Result.success(sysUserApiService.findByUsername(username));
    }

    @SaIgnore
    @GetMapping(value = "/getUserRole")
    @Operation(summary = "查询当前用户的角色", description = "查询当前用户的角色")
    public Result<List<Long>> getUserRole(@RequestParam(value = "userId") Long userId) {
        return Result.success(sysUserApiService.getUserRole(userId));
    }

    @SaIgnore
    @GetMapping(value = "/getUserRolePermission")
    @Operation(summary = "查询用户角色权限", description = "查询用户角色权限")
    public Result<List<String>> getUserRolePermission(@RequestParam(value = "roleId",required = false) Long roleId) {
        return Result.success(sysUserApiService.getUserRolePermission(roleId));
    }

    @GetMapping(value = "/getUserRoleCode")
    @Operation(summary = "查询用户角色编码", description = "根据用户ID查询用户角色编码")
    public Result<List<String>> getUserRoleCode(@RequestParam(value = "userId") Long userId) {
        return Result.success(sysUserApiService.getUserRoleCode(userId));
    }
}
