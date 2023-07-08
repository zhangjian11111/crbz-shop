package cn.crbz.controller.other;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.system.entity.dos.Logistics;
import cn.crbz.modules.system.service.LogisticsService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 管理端,物流公司接口
 *
 * @author Chopper
 * @since 2020/11/17 7:56 下午
 */
@RestController
@Api(tags = "管理端,物流公司接口")
@RequestMapping("/manager/other/logistics")
public class LogisticsManagerController {
    @Autowired
    private LogisticsService logisticsService;

    @ApiOperation(value = "通过id获取物流公司")
    @GetMapping(value = "/get/{id}")
    public ResultMessage<Logistics> get(@PathVariable String id) {
        return ResultUtil.data(logisticsService.getById(id));
    }

    @ApiOperation(value = "分页获取物流公司")
    @GetMapping(value = "/getByPage")
    public ResultMessage<IPage<Logistics>> getByPage(PageVO page) {
        return ResultUtil.data(logisticsService.page(PageUtil.initPage(page)));
    }

    @ApiOperation(value = "编辑物流公司")
    @ApiImplicitParam(name = "id", value = "物流公司ID", required = true, paramType = "path", dataType = "string")
    @PutMapping(value = "/{id}")
    public ResultMessage<Logistics> update(@NotNull @PathVariable String id, @Valid Logistics logistics) {
        logistics.setId(id);
        logisticsService.updateById(logistics);
        return ResultUtil.data(logistics);
    }

    @ApiOperation(value = "添加物流公司")
    @PostMapping(value = "/save")
    public ResultMessage<Logistics> save(@Valid Logistics logistics) {
        logisticsService.save(logistics);
        return ResultUtil.data(logistics);
    }

    @ApiOperation(value = "删除物流公司")
    @ApiImplicitParam(name = "id", value = "物流公司ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/delete/{id}")
    public ResultMessage<Object> delAllByIds(@PathVariable String id) {
        logisticsService.removeById(id);
        return ResultUtil.success();
    }
}
