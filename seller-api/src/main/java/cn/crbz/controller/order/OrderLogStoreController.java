package cn.crbz.controller.order;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.OperationalJudgment;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.order.service.OrderService;
import cn.crbz.modules.order.trade.entity.dos.OrderLog;
import cn.crbz.modules.order.trade.service.OrderLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 店铺端,订单日志接口
 *
 * @author Chopper
 * @since 2020/12/5
 **/
@RestController
@Api(tags = "店铺端,订单日志接口")
@RequestMapping("/store/order/orderLog")
public class OrderLogStoreController {

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "通过订单编号获取订单日志")
    @ApiImplicitParam(name = "orderSn", value = "订单编号", required = true, paramType = "path")
    @GetMapping(value = "/{orderSn}")
    public ResultMessage<List<OrderLog>> get(@PathVariable String orderSn) {
        OperationalJudgment.judgment(orderService.getBySn(orderSn));
        return ResultUtil.data(orderLogService.getOrderLog(orderSn));
    }
}
