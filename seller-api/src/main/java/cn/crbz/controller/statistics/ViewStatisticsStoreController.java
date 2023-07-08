package cn.crbz.controller.statistics;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.statistics.entity.dto.StatisticsQueryParam;
import cn.crbz.modules.statistics.entity.vo.PlatformViewVO;
import cn.crbz.modules.statistics.service.PlatformViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 店铺端,流量统计接口
 *
 * @author Chopper
 * @since 2021/2/9 11:19
 */
@Api(tags = "店铺端,流量统计接口")
@RestController
@RequestMapping("/store/statistics/view")
public class ViewStatisticsStoreController {
    @Autowired
    private PlatformViewService platformViewService;

    @ApiOperation(value = "流量数据 表单获取")
    @GetMapping("/list")
    public ResultMessage<List<PlatformViewVO>> getByPage(StatisticsQueryParam queryParam) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        queryParam.setStoreId(storeId);
        return ResultUtil.data(platformViewService.list(queryParam));
    }
}
