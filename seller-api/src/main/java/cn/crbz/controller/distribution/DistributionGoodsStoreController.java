package cn.crbz.controller.distribution;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.OperationalJudgment;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.distribution.entity.dos.DistributionGoods;
import cn.crbz.modules.distribution.entity.dos.DistributionSelectedGoods;
import cn.crbz.modules.distribution.entity.dto.DistributionGoodsSearchParams;
import cn.crbz.modules.distribution.entity.vos.DistributionGoodsVO;
import cn.crbz.modules.distribution.service.DistributionGoodsService;
import cn.crbz.modules.distribution.service.DistributionSelectedGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 店铺端,分销商品接口
 *
 * @author Bulbasaur
 * @since 2020/11/16 10:06 下午
 */
@RestController
@Api(tags = "店铺端,分销商品接口")
@RequestMapping("/store/distribution/goods")
public class DistributionGoodsStoreController {

    /**
     * 分销商品
     */
    @Autowired
    private DistributionGoodsService distributionGoodsService;

    /**
     * 已选择分销商品
     */
    @Autowired
    private DistributionSelectedGoodsService distributionSelectedGoodsService;

    @ApiOperation(value = "获取分销商商品列表")
    @GetMapping
    public ResultMessage<IPage<DistributionGoodsVO>> distributionGoods(DistributionGoodsSearchParams distributionGoodsSearchParams) {

        return ResultUtil.data(distributionGoodsService.goodsPage(distributionGoodsSearchParams));
    }

    @ApiOperation(value = "选择商品参与分销")
    @ApiImplicitParam(name = "skuId", value = "规格ID", required = true, dataType = "String", paramType = "path")
    @PutMapping(value = "/checked/{skuId}")
    public ResultMessage<DistributionGoods> distributionCheckGoods(@NotNull(message = "规格ID不能为空") @PathVariable String skuId,
                                                                   @NotNull(message = "佣金金额不能为空") @RequestParam Double commission) {

        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        return ResultUtil.data(distributionGoodsService.checked(skuId, commission, storeId));
    }

    @ApiOperation(value = "取消分销商品")
    @ApiImplicitParam(name = "id", value = "分销商商品ID", required = true, paramType = "path")
    @DeleteMapping(value = "/cancel/{id}")
    public ResultMessage<Object> cancel(@NotNull @PathVariable String id) {
        OperationalJudgment.judgment(distributionGoodsService.getById(id));
        //清除分销商已选择分销商品
        distributionSelectedGoodsService.remove(new QueryWrapper<DistributionSelectedGoods>().eq("distribution_goods_id", id));
        //清除分销商品
        distributionGoodsService.removeById(id);
        return ResultUtil.success();
    }

}
