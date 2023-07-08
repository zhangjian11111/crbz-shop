package cn.crbz.controller.statistics;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.aftersale.entity.dos.AfterSale;
import cn.crbz.modules.order.order.entity.vo.OrderSimpleVO;
import cn.crbz.modules.statistics.entity.dto.StatisticsQueryParam;
import cn.crbz.modules.statistics.entity.vo.OrderOverviewVO;
import cn.crbz.modules.statistics.entity.vo.OrderStatisticsDataVO;
import cn.crbz.modules.statistics.service.AfterSaleStatisticsService;
import cn.crbz.modules.statistics.service.OrderStatisticsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理端,订单统计接口
 *
 * @author Bulbasaur
 * @since 2020/12/9 19:04
 */
@Slf4j
@Api(tags = "管理端,订单统计接口")
@RestController
@RequestMapping("/manager/statistics/order")
public class OrderStatisticsManagerController {
    @Autowired
    private OrderStatisticsService orderStatisticsService;
    @Autowired
    private AfterSaleStatisticsService afterSaleStatisticsService;

    @ApiOperation(value = "订单概览统计")
    @GetMapping("/overview")
    public ResultMessage<OrderOverviewVO> overview(StatisticsQueryParam statisticsQueryParam) {
        try {
            return ResultUtil.data(orderStatisticsService.overview(statisticsQueryParam));
        } catch (Exception e) {
            log.error("订单概览统计错误",e);
        }
        return null;
    }

    @ApiOperation(value = "订单图表统计")
    @GetMapping
    public ResultMessage<List<OrderStatisticsDataVO>> statisticsChart(StatisticsQueryParam statisticsQueryParam) {
        try {
            return ResultUtil.data(orderStatisticsService.statisticsChart(statisticsQueryParam));
        } catch (Exception e) {
            log.error("订单图表统计",e);
        }
        return null;
    }


    @ApiOperation(value = "订单统计")
    @GetMapping("/order")
    public ResultMessage<IPage<OrderSimpleVO>> order(StatisticsQueryParam statisticsQueryParam, PageVO pageVO) {
        try {
            return ResultUtil.data(orderStatisticsService.getStatistics(statisticsQueryParam, pageVO));
        } catch (Exception e) {
            log.error("订单统计",e);
        }
        return null;
    }


    @ApiOperation(value = "退单统计")
    @GetMapping("/refund")
    public ResultMessage<IPage<AfterSale>> refund(StatisticsQueryParam statisticsQueryParam, PageVO pageVO) {
        return ResultUtil.data(afterSaleStatisticsService.getStatistics(statisticsQueryParam, pageVO));
    }
}
