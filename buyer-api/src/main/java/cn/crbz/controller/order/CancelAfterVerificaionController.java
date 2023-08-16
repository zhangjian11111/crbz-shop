package cn.crbz.controller.order;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.order.entity.dto.OrderSearchParams;
import cn.crbz.modules.order.order.entity.vo.OrderSimpleVO;
import cn.crbz.modules.order.order.service.OrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 买家端,为了方便小程序核销，小程序设置只允许管理员核销订单
 * @author zhangjian
 * @since 2023/07/29 1:55 凌晨
 **/
@Slf4j
@RestController
@RequestMapping("/buyer/order/order")
@Api(tags = "买家端，店员核销订单")
public class CancelAfterVerificaionController {

    /**
     * 订单
     */
    @Autowired
    private OrderService orderService;


    @PreventDuplicateSubmissions
    @ApiOperation(value = "订单核验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderSn", value = "订单号", required = true, paramType = "path"),
            @ApiImplicitParam(name = "verificationCode", value = "核验码", required = true, paramType = "path")
    })
    @PutMapping(value = "/mpTake/{orderSn}/{verificationCode}")
    public ResultMessage<Object> mpTake(@PathVariable String orderSn, @PathVariable String verificationCode) {
        log.info("核销参数---注意参数检查是否有空格：：："+orderSn+"***"+verificationCode);
        return ResultUtil.data(orderService.mpTake(orderSn,verificationCode));
    }


    @ApiOperation(value = "查询所有订单列表")
    @GetMapping(value = "/showAllOrders")
    public ResultMessage<IPage<OrderSimpleVO>> queryAllOrder(OrderSearchParams orderSearchParams) {
        return ResultUtil.data(orderService.queryByParams(orderSearchParams));
    }


}
