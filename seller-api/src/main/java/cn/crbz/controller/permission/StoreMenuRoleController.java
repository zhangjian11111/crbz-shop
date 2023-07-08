package cn.crbz.controller.permission;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.dos.StoreMenuRole;
import cn.crbz.modules.member.service.StoreMenuRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 店铺端,角色菜单接口
 *
 * @author Chopper
 * @since 2020/11/22 11:40
 */
@RestController
@Api(tags = "店铺端,角色菜单接口")
@RequestMapping("/store/roleMenu")
public class StoreMenuRoleController {
    @Autowired
    private StoreMenuRoleService storeMenuRoleService;

    @GetMapping(value = "/{roleId}")
    @ApiOperation(value = "查看某角色拥有到菜单")
    public ResultMessage<List<StoreMenuRole>> get(@PathVariable String roleId) {
        return ResultUtil.data(storeMenuRoleService.findByRoleId(roleId));
    }

    @PostMapping(value = "/{roleId}")
    @ApiOperation(value = "保存角色菜单")
    public ResultMessage save(@PathVariable String roleId, @RequestBody List<StoreMenuRole> roleMenus) {
        storeMenuRoleService.updateRoleMenu(roleId, roleMenus);
        return ResultUtil.success();
    }

}
