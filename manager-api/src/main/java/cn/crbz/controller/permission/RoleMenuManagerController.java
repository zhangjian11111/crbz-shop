package cn.crbz.controller.permission;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.permission.entity.dos.RoleMenu;
import cn.crbz.modules.permission.service.RoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 管理端,角色菜单接口
 *
 * @author Chopper
 * @since 2020/11/22 11:40
 */
@RestController
@Api(tags = "管理端,角色菜单接口")
@RequestMapping("/manager/permission/roleMenu")
public class RoleMenuManagerController {
    @Autowired
    private RoleMenuService roleMenuService;

    @GetMapping(value = "/{roleId}")
    @ApiOperation(value = "查看某角色拥有到菜单")
    public ResultMessage<List<RoleMenu>> get(@PathVariable String roleId) {
        return ResultUtil.data(roleMenuService.findByRoleId(roleId));
    }

    @PostMapping(value = "/{roleId}")
    @ApiOperation(value = "保存角色菜单")
    public ResultMessage save(@PathVariable String roleId, @RequestBody List<RoleMenu> roleMenus) {
        roleMenuService.updateRoleMenu(roleId, roleMenus);
        return ResultUtil.success();
    }

}
