package cn.crbz.controller.permission;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.common.vo.SearchVO;
import cn.crbz.modules.member.entity.dos.StoreDepartment;
import cn.crbz.modules.member.entity.vo.StoreDepartmentVO;
import cn.crbz.modules.member.service.StoreDepartmentService;
import cn.crbz.mybatis.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 管理端,部门管理接口
 *
 * @author Chopper
 * @since 2020/11/22 12:06
 */
@RestController
@Api(tags = "店铺端,部门管理接口")
@RequestMapping("/store/department")
public class StoreDepartmentController {
    @Autowired
    private StoreDepartmentService storeDepartmentService;

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查看部门详情")
    public ResultMessage<StoreDepartment> get(@PathVariable String id) {
        StoreDepartment storeDepartment = storeDepartmentService.getById(id);
        return ResultUtil.data(storeDepartment);
    }

    @GetMapping
    @ApiOperation(value = "获取树状结构")
    public ResultMessage<List<StoreDepartmentVO>> getByPage(StoreDepartment entity,
                                                            SearchVO searchVo) {
        entity.setStoreId(UserContext.getCurrentUser().getStoreId());
        return ResultUtil.data(storeDepartmentService.tree(PageUtil.initWrapper(entity, searchVo)));

    }

    @PostMapping
    @ApiOperation(value = "新增部门")
    public ResultMessage<StoreDepartment> save(StoreDepartment storeDepartment) {
        storeDepartment.setStoreId(UserContext.getCurrentUser().getStoreId());
        storeDepartmentService.save(storeDepartment);
        return ResultUtil.data(storeDepartment);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新部门")
    public ResultMessage<StoreDepartment> update(@PathVariable String id, StoreDepartment storeDepartment) {
        storeDepartment.setId(id);
        storeDepartmentService.update(storeDepartment);
        return ResultUtil.data(storeDepartment);
    }

    @DeleteMapping(value = "/{ids}")
    @ApiOperation(value = "删除部门")
    public ResultMessage<Object> delAllByIds(@PathVariable List<String> ids) {
        storeDepartmentService.deleteByIds(ids);
        return ResultUtil.success();
    }
}
