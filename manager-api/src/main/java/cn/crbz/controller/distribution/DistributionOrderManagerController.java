package cn.crbz.controller.distribution;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.distribution.entity.dos.DistributionOrder;
import cn.crbz.modules.distribution.entity.vos.DistributionOrderSearchParams;
import cn.crbz.modules.distribution.service.DistributionOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,分销订单管理接口
 *
 * @author pikachu
 * @since 2020-03-14 23:04:56
 */
@RestController
@Api(tags = "管理端,分销订单管理接口")
@RequestMapping("/manager/distribution/order")
public class DistributionOrderManagerController {

    @Autowired
    private DistributionOrderService distributionOrderService;

    @ApiOperation(value = "通过id获取分销订单")
    @GetMapping(value = "/get/{id}")
    public ResultMessage<DistributionOrder> get(@PathVariable String id) {

        return ResultUtil.data(distributionOrderService.getById(id));
    }


    @ApiOperation(value = "分页获取分销订单")
    @GetMapping(value = "/getByPage")
    public ResultMessage<IPage<DistributionOrder>> getByPage(DistributionOrderSearchParams distributionOrderSearchParams) {

        return ResultUtil.data(distributionOrderService.getDistributionOrderPage(distributionOrderSearchParams));
    }
}
