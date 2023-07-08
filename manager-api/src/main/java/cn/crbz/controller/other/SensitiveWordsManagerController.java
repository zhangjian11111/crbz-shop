package cn.crbz.controller.other;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.system.entity.dos.SensitiveWords;
import cn.crbz.modules.system.service.SensitiveWordsService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理端,敏感词管理接口
 *
 * @author Bulbasaur
 * @since 2020-05-06 15:18:56
 */
@RestController
@Api(tags = "管理端,敏感词管理接口")
@RequestMapping("/manager/other/sensitiveWords")
public class SensitiveWordsManagerController {

    @Autowired
    private SensitiveWordsService sensitiveWordsService;

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "敏感词ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/get/{id}")
    public ResultMessage<SensitiveWords> get(@PathVariable String id) {
        return ResultUtil.data(sensitiveWordsService.getById(id));
    }

    @ApiOperation(value = "分页获取")
    @GetMapping
    public ResultMessage<IPage<SensitiveWords>> getByPage(PageVO page) {
        return ResultUtil.data(sensitiveWordsService.page(PageUtil.initPage(page)));
    }

    @ApiOperation(value = "添加敏感词")
    @PostMapping
    public ResultMessage<SensitiveWords> add(@Valid SensitiveWords sensitiveWords) {
        sensitiveWordsService.save(sensitiveWords);
        sensitiveWordsService.resetCache();
        return ResultUtil.data(sensitiveWords);
    }

    @ApiOperation(value = "修改敏感词")
    @ApiImplicitParam(name = "id", value = "敏感词ID", required = true, dataType = "String", paramType = "path")
    @PutMapping("/{id}")
    public ResultMessage<SensitiveWords> edit(@PathVariable String id, SensitiveWords sensitiveWords) {
        sensitiveWords.setId(id);
        sensitiveWordsService.updateById(sensitiveWords);
        sensitiveWordsService.resetCache();
        return ResultUtil.data(sensitiveWords);
    }

    @ApiOperation(value = "批量删除")
    @ApiImplicitParam(name = "ids", value = "敏感词ID", required = true, dataType = "String", allowMultiple = true, paramType = "path")
    @DeleteMapping(value = "/delByIds/{ids}")
    public ResultMessage<Object> delAllByIds(@PathVariable List<String> ids) {
        sensitiveWordsService.removeByIds(ids);
        sensitiveWordsService.resetCache();
        return ResultUtil.success();
    }
}
