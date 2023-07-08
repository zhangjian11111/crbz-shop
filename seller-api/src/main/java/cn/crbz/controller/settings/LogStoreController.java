package cn.crbz.controller.settings;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.common.vo.SearchVO;
import cn.crbz.modules.permission.service.SystemLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


/**
 * 店铺端,日志管理接口
 *
 * @author Chopper
 * @since 2020/11/22 14:23
 */
@RestController
@Api(tags = "店铺端,日志管理接口")
@RequestMapping("/store/settings/log")
public class LogStoreController {
    @Autowired
    private SystemLogService systemLogService;

    @GetMapping(value = "/getAllByPage")
    @ApiOperation(value = "分页获取全部")
    public ResultMessage<Object> getAllByPage(@RequestParam(required = false) Integer type,
                                              @RequestParam String key,
                                              String operatorName,
                                              SearchVO searchVo,
                                              PageVO pageVo) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        return ResultUtil.data(systemLogService.queryLog(storeId, operatorName, key, searchVo, pageVo));
    }
}
