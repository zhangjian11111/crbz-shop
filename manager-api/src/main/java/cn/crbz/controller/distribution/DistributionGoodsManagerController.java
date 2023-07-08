package cn.crbz.controller.distribution;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.distribution.entity.dto.DistributionGoodsSearchParams;
import cn.crbz.modules.distribution.entity.vos.DistributionGoodsVO;
import cn.crbz.modules.distribution.service.DistributionGoodsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,分销商品管理接口
 *
 * @author pikachu
 * @since 2020-03-14 23:04:56
 */
@RestController
@Api(tags = "管理端,分销商品管理接口")
@RequestMapping("/manager/distribution/goods")
public class DistributionGoodsManagerController {

    @Autowired
    private DistributionGoodsService distributionGoodsService;

    @GetMapping(value = "/getByPage")
    @ApiOperation(value = "分页获取")
    public ResultMessage<IPage<DistributionGoodsVO>> getByPage(DistributionGoodsSearchParams distributionGoodsSearchParams) {
        return ResultUtil.data(distributionGoodsService.goodsPage(distributionGoodsSearchParams));
    }


    @DeleteMapping(value = "/delByIds/{ids}")
    @ApiOperation(value = "批量删除")
    public ResultMessage<Object> delAllByIds(@PathVariable List ids) {

        distributionGoodsService.removeByIds(ids);
        return ResultUtil.success();
    }
}
