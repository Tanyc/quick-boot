package com.quick.modules.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.quick.common.aspect.annotation.PreAuth;
import com.quick.common.controller.SuperController;
import com.quick.common.vo.Result;
import com.quick.modules.system.entity.SysMenu;
import com.quick.modules.system.service.ISysMenuService;
import com.quick.modules.system.vo.SysMenuTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/SysMenu")
@RequiredArgsConstructor
@Tag(name = "菜单信息")
@PreAuth(replace = "SysMenu:")
public class SysMenuController extends SuperController<ISysMenuService, SysMenu, String> {

    @SaIgnore
    @GetMapping(value = "/getRoutes")
    @Operation(summary = "查询所有路由", description = "查询所有路由")
    public Result<List<SysMenuTreeVO>> getRoutes() {
        return Result.success(baseService.getRoutes());
    }

    @GetMapping(value = "/getUserMenuTree")
    @Operation(summary = "查询当前登录用户拥有的菜单树和按钮权限", description = "查询当前登录用户拥有的菜单树和按钮权限")
    public Result<List<SysMenuTreeVO>> getUserMenuTree() {
        String userId = StpUtil.getLoginId().toString();
        return Result.success(baseService.getUserMenuTree(userId));
    }

    @GetMapping(value = "/getUserMenu")
    @Operation(summary = "查询当前登录用户拥有的菜单树和按钮权限", description = "查询当前登录用户拥有的菜单树和按钮权限")
    public Result<List<SysMenu>> getUserMenu() {
        String userId = StpUtil.getLoginId().toString();
        return Result.success(baseService.getUserMenu(userId));
    }

}
