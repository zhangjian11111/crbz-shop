package cn.crbz.controller.promotion;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.promotion.entity.dos.PromotionGoods;
import cn.crbz.modules.promotion.entity.dto.search.PromotionGoodsSearchParams;
import cn.crbz.modules.promotion.entity.enums.PromotionsStatusEnum;
import cn.crbz.modules.promotion.service.PromotionGoodsService;
import cn.crbz.modules.promotion.service.PromotionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 管理端,促销接口
 *
 * @author paulG
 * @since 2021/2/2
 **/
@RestController
@Api(tags = "管理端,促销接口")
@RequestMapping("/manager/promotion/promotion")
public class PromotionManagerController {

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private PromotionGoodsService promotionGoodsService;

    @GetMapping("/current")
    @ApiOperation(value = "获取当前进行中的促销活动")
    public ResultMessage<Map<String, List<PromotionGoods>>> getCurrentPromotion() {
        Map<String, List<PromotionGoods>> currentPromotion = promotionService.getCurrentPromotion();
        return ResultUtil.data(currentPromotion);
    }

    @GetMapping("/{promotionId}/goods")
    @ApiOperation(value = "获取当前进行中的促销活动商品")
    public ResultMessage<IPage<PromotionGoods>> getPromotionGoods(@PathVariable String promotionId, String promotionType, PageVO pageVO) {
        PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
        searchParams.setPromotionId(promotionId);
        searchParams.setPromotionType(promotionType);
        searchParams.setPromotionStatus(PromotionsStatusEnum.START.name());
        IPage<PromotionGoods> promotionGoods = promotionGoodsService.pageFindAll(searchParams, pageVO);
        return ResultUtil.data(promotionGoods);
    }


}
