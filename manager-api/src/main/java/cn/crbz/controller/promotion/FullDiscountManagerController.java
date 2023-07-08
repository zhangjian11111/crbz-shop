package cn.crbz.controller.promotion;

import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.cart.entity.vo.FullDiscountVO;
import cn.crbz.modules.promotion.entity.dos.FullDiscount;
import cn.crbz.modules.promotion.entity.dto.search.FullDiscountSearchParams;
import cn.crbz.modules.promotion.service.FullDiscountService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 管理端,满额活动接口
 *
 * @author paulG
 * @since 2021/1/12
 **/
@RestController
@Api(tags = "管理端,满额活动接口")
@RequestMapping("/manager/promotion/fullDiscount")
public class FullDiscountManagerController {

    @Autowired
    private FullDiscountService fullDiscountService;

    @ApiOperation(value = "获取满优惠列表")
    @GetMapping
    public ResultMessage<IPage<FullDiscount>> getCouponList(FullDiscountSearchParams searchParams, PageVO page) {
        return ResultUtil.data(fullDiscountService.pageFindAll(searchParams, page));
    }

    @ApiOperation(value = "获取满优惠详情")
    @GetMapping("/{id}")
    public ResultMessage<FullDiscountVO> getCouponDetail(@PathVariable String id) {
        return ResultUtil.data(fullDiscountService.getFullDiscount(id));
    }

    @ApiOperation(value = "修改满额活动状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "满额活动ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "promotionStatus", value = "满额活动状态", required = true, paramType = "path")
    })
    @PutMapping("/status/{id}")
    public ResultMessage<Object> updateCouponStatus(@PathVariable String id, Long startTime, Long endTime) {
        if (fullDiscountService.updateStatus(Collections.singletonList(id), startTime, endTime)) {
            return ResultUtil.success(ResultCode.SUCCESS);
        }
        return ResultUtil.error(ResultCode.ERROR);
    }
}
