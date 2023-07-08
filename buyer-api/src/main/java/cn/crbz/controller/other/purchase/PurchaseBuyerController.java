package cn.crbz.controller.other.purchase;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.goods.entity.dos.GoodsUnit;
import cn.crbz.modules.goods.service.GoodsUnitService;
import cn.crbz.modules.purchase.entity.dos.PurchaseOrder;
import cn.crbz.modules.purchase.entity.params.PurchaseOrderSearchParams;
import cn.crbz.modules.purchase.entity.vos.PurchaseOrderVO;
import cn.crbz.modules.purchase.service.PurchaseOrderService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 买家端,采购接口
 *
 * @author Chopper
 * @since 2020/11/16 10:06 下午
 */
@Api(tags = "买家端,采购接口")
@RestController
@RequestMapping("/buyer/other/purchase/purchase")
public class PurchaseBuyerController {

    /**
     * 采购单
     */
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private GoodsUnitService goodsUnitService;


    @ApiOperation(value = "分页获取商品计量单位")
    @GetMapping("/goodsUnit")
    public ResultMessage<IPage<GoodsUnit>> goodsUnitPage(PageVO pageVO) {
        return ResultUtil.data(goodsUnitService.page(PageUtil.initPage(pageVO)));
    }


    @PreventDuplicateSubmissions
    @ApiOperation(value = "添加采购单")
    @PostMapping
    public ResultMessage<PurchaseOrderVO> addPurchaseOrderVO(@RequestBody PurchaseOrderVO purchaseOrderVO) {
        return ResultUtil.data(purchaseOrderService.addPurchaseOrder(purchaseOrderVO));
    }

    @ApiOperation(value = "采购单分页")
    @GetMapping
    public ResultMessage<IPage<PurchaseOrder>> get(PurchaseOrderSearchParams purchaseOrderSearchParams) {
        return ResultUtil.data(purchaseOrderService.page(purchaseOrderSearchParams));
    }

    @ApiOperation(value = "采购单详情")
    @ApiImplicitParam(name = "id", value = "采购单ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/{id}")
    public ResultMessage<PurchaseOrderVO> getPurchaseOrder(@NotNull @PathVariable String id) {
        return ResultUtil.data(purchaseOrderService.getPurchaseOrder(id));
    }

    @ApiOperation(value = "会员采购单分页")
    @GetMapping("/getByMember")
    public ResultMessage<IPage<PurchaseOrder>> getByMember(PurchaseOrderSearchParams purchaseOrderSearchParams) {
        purchaseOrderSearchParams.setMemberId(UserContext.getCurrentUser().getId());
        return ResultUtil.data(purchaseOrderService.page(purchaseOrderSearchParams));
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "关闭采购单")
    @ApiImplicitParam(name = "id", value = "采购单ID", required = true, dataType = "Long", paramType = "path")
    @PutMapping("/{id}")
    public ResultMessage<Object> close(@NotNull @PathVariable String id) {
        purchaseOrderService.close(id);
        return ResultUtil.success(ResultCode.SUCCESS);
    }

}
