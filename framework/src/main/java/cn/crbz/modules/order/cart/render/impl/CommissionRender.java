package cn.crbz.modules.order.cart.render.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.crbz.common.enums.PromotionTypeEnum;
import cn.crbz.common.utils.CurrencyUtil;
import cn.crbz.modules.goods.service.CategoryService;
import cn.crbz.modules.order.cart.entity.dto.TradeDTO;
import cn.crbz.modules.order.cart.entity.enums.CartTypeEnum;
import cn.crbz.modules.order.cart.entity.enums.RenderStepEnums;
import cn.crbz.modules.order.cart.entity.vo.CartSkuVO;
import cn.crbz.modules.order.cart.entity.vo.CartVO;
import cn.crbz.modules.order.cart.render.CartRenderStep;
import cn.crbz.modules.order.order.entity.dto.PriceDetailDTO;
import cn.crbz.modules.promotion.entity.dos.KanjiaActivityGoods;
import cn.crbz.modules.promotion.entity.dos.PointsGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 佣金计算
 *
 * @author Chopper
 * @see CartVO
 */
@Service
public class CommissionRender implements CartRenderStep {


    /**
     * 商品分类
     */
    @Autowired
    private CategoryService categoryService;

    @Override
    public RenderStepEnums step() {
        return RenderStepEnums.PLATFORM_COMMISSION;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        buildCartPrice(tradeDTO);
    }

    /**
     * 购物车佣金计算
     *
     * @param tradeDTO 购物车展示信息
     */
    void buildCartPrice(TradeDTO tradeDTO) {
        //购物车列表
        List<CartVO> cartVOS = tradeDTO.getCartList();

        //计算购物车价格
        for (CartVO cart : cartVOS) {
            //累加价格
            for (CartSkuVO cartSkuVO : cart.getCheckedSkuList()) {

                PriceDetailDTO priceDetailDTO = cartSkuVO.getPriceDetailDTO();
                //平台佣金根据分类计算
                String categoryId = cartSkuVO.getGoodsSku().getCategoryPath()
                        .substring(cartSkuVO.getGoodsSku().getCategoryPath().lastIndexOf(",") + 1);
                if (CharSequenceUtil.isNotEmpty(categoryId)) {
                    Double commissionRate = categoryService.getCategoryById(categoryId).getCommissionRate();
                    priceDetailDTO.setPlatFormCommissionPoint(commissionRate);
                }

                //如果积分订单 积分订单，单独操作订单结算金额和商家结算字段
                if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.POINTS) && tradeDTO.getSkuList().get(0).getPromotionMap() != null && !tradeDTO.getSkuList().get(0).getPromotionMap().isEmpty()) {
                    Optional<Map.Entry<String, Object>> pointsPromotions = tradeDTO.getSkuList().get(0).getPromotionMap().entrySet().stream().filter(i -> i.getKey().contains(PromotionTypeEnum.POINTS_GOODS.name())).findFirst();
                    if (pointsPromotions.isPresent()) {
                        JSONObject promotionsObj = JSONUtil.parseObj(pointsPromotions.get().getValue());
                        PointsGoods pointsGoods = JSONUtil.toBean(promotionsObj, PointsGoods.class);
                        priceDetailDTO.setSettlementPrice(pointsGoods.getSettlementPrice());
                    }
                }
                //如果砍价订单 计算金额，单独操作订单结算金额和商家结算字段
                else if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.KANJIA) && tradeDTO.getSkuList().get(0).getPromotionMap() != null && !tradeDTO.getSkuList().get(0).getPromotionMap().isEmpty()) {
                    Optional<Map.Entry<String, Object>> kanjiaPromotions = tradeDTO.getSkuList().get(0).getPromotionMap().entrySet().stream().filter(i -> i.getKey().contains(PromotionTypeEnum.KANJIA.name())).findFirst();
                    if (kanjiaPromotions.isPresent()) {
                        JSONObject promotionsObj = JSONUtil.parseObj(kanjiaPromotions.get().getValue());
                        KanjiaActivityGoods kanjiaActivityGoods = JSONUtil.toBean(promotionsObj, KanjiaActivityGoods.class);
                        priceDetailDTO.setSettlementPrice(CurrencyUtil.add(kanjiaActivityGoods.getSettlementPrice(),priceDetailDTO.getBillPrice()));
                    }
                }
            }
        }
    }


}
