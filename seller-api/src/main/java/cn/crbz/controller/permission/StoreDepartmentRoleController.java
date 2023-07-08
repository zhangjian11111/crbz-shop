package cn.crbz.controller.permission;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.dos.StoreDepartmentRole;
import cn.crbz.modules.member.service.StoreDepartmentRoleService;
import cn.crbz.modules.permission.entity.dos.DepartmentRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 店铺端,部门角色接口
 *
 * @author Chopper
 * @since 2020/11/22 14:05
 */
@RestController
@Api(tags = "店铺端,部门角色接口")
@RequestMapping("/store/departmentRole")
public class StoreDepartmentRoleController {
    @Autowired
    private StoreDepartmentRoleService storeDepartmentRoleService;

    @GetMapping(value = "/{departmentId}")
    @ApiOperation(value = "查看部门拥有的角色")
    public ResultMessage<List<StoreDepartmentRole>> get(@PathVariable String departmentId) {
        return ResultUtil.data(storeDepartmentRoleService.listByDepartmentId(departmentId));
    }

    @PutMapping("/{departmentId}")
    @ApiOperation(value = "更新部门角色")
    public ResultMessage<DepartmentRole> update(@PathVariable String departmentId, @RequestBody List<StoreDepartmentRole> storeDepartmentRoles) {
        storeDepartmentRoleService.updateByDepartmentId(departmentId, storeDepartmentRoles);
        return ResultUtil.success();
    }

}
