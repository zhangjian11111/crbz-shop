package cn.crbz.controller.distribution;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.distribution.entity.dto.DistributionGoodsSearchParams;
import cn.crbz.modules.distribution.entity.vos.DistributionGoodsVO;
import cn.crbz.modules.distribution.service.DistributionGoodsService;
import cn.crbz.modules.distribution.service.DistributionSelectedGoodsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 买家端,分销商品接口
 *
 * @author Bulbasaur
 * @since 2020/11/16 10:06 下午
 */
@RestController
@Api(tags = "买家端,分销商品接口")
@RequestMapping("/buyer/distribution/goods")
public class DistributionGoodsBuyerController {

    /**
     * 分销商品
     */
    @Autowired
    private DistributionGoodsService distributionGoodsService;
    /**
     * 选择分销商品
     */
    @Autowired
    private DistributionSelectedGoodsService distributionSelectedGoodsService;


    @ApiOperation(value = "获取分销商商品列表")
    @GetMapping
    public ResultMessage<IPage<DistributionGoodsVO>> distributionGoods(DistributionGoodsSearchParams distributionGoodsSearchParams) {
        return ResultUtil.data(distributionGoodsService.goodsPage(distributionGoodsSearchParams));
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "选择分销商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "distributionGoodsId", value = "分销ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "checked", value = "是否选择", required = true, dataType = "boolean", paramType = "query")
    })
    @GetMapping(value = "/checked/{distributionGoodsId}")
    public ResultMessage<Object> distributionCheckGoods(
            @NotNull(message = "分销商品不能为空") @PathVariable("distributionGoodsId") String distributionGoodsId, Boolean checked) {
        Boolean result = false;
        if (checked) {
            result = distributionSelectedGoodsService.add(distributionGoodsId);
        } else {
            result = distributionSelectedGoodsService.delete(distributionGoodsId);
        }
        //判断操作结果
        if (result) {
            return ResultUtil.success(ResultCode.SUCCESS);
        } else {
            throw new ServiceException(ResultCode.ERROR);
        }

    }
}
