package cn.crbz.controller.permission;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.dos.StoreRole;
import cn.crbz.modules.member.service.StoreRoleService;
import cn.crbz.modules.permission.entity.dos.Role;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 店铺端,角色管理接口
 *
 * @author Chopper
 * @since 2020/11/20 18:50
 */
@RestController
@Api(tags = "店铺端,店铺角色管理接口")
@RequestMapping("/store/role")
public class StoreRoleController {
    @Autowired
    private StoreRoleService storeRoleService;

    @PostMapping
    @ApiOperation(value = "添加角色")
    public ResultMessage<StoreRole> add(StoreRole storeRole) {
        storeRoleService.saveStoreRole(storeRole);
        return ResultUtil.data(storeRole);
    }

    @GetMapping
    @ApiOperation(value = "查询店铺角色")
    public ResultMessage<Page> page(PageVO pageVo, StoreRole storeRole) {
        storeRole.setStoreId(UserContext.getCurrentUser().getStoreId());
        Page page = storeRoleService.page(PageUtil.initPage(pageVo), PageUtil.initWrapper(storeRole));
        return ResultUtil.data(page);
    }

    @PutMapping("/{roleId}")
    @ApiOperation(value = "编辑店铺角色")
    public ResultMessage<StoreRole> edit(@PathVariable String roleId, StoreRole storeRole) {
        storeRole.setId(roleId);
        storeRoleService.update(storeRole);
        return ResultUtil.data(storeRole);
    }

    @DeleteMapping(value = "/{ids}")
    @ApiOperation(value = "批量删除店铺角色")
    public ResultMessage<Role> delByIds(@PathVariable List<String> ids) {
        storeRoleService.deleteRoles(ids);
        return ResultUtil.success();
    }


}
