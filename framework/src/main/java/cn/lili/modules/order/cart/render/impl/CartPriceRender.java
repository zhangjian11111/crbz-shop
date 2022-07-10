package cn.lili.modules.order.cart.render.impl;

import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.member.entity.dos.MemberAddress;
import cn.lili.modules.order.cart.entity.dto.TradeDTO;
import cn.lili.modules.order.cart.entity.enums.RenderStepEnums;
import cn.lili.modules.order.cart.entity.vo.CartSkuVO;
import cn.lili.modules.order.cart.entity.vo.CartVO;
import cn.lili.modules.order.cart.render.CartRenderStep;
import cn.lili.modules.order.order.entity.dto.PriceDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车渲染，将购物车中的各个商品，拆分到每个商家，形成购物车VO
 *
 * @author Chopper
 * @see CartVO
 */
@Service
@Slf4j
public class CartPriceRender implements CartRenderStep {

    @Override
    public RenderStepEnums step() {
        return RenderStepEnums.CART_PRICE;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        List<CartSkuVO> notsupportMessage = new ArrayList<>();
        CartSkuVO cartSkuVO = new CartSkuVO();
        GoodsSku goodsSku = new GoodsSku();

        //价格过滤 在购物车商品失效时，需要对价格进行初始化操作
        initPriceDTO(tradeDTO);

        //构造cartVO
        buildCartPrice(tradeDTO);
        buildTradePrice(tradeDTO);
        if (tradeDTO.getPriceDetailDTO().getGoodsPrice() < 25) {
            if (tradeDTO.getPriceDetailDTO().getGoodsPrice() < 25){
                log.info("总金额为: "+tradeDTO.getPriceDetailDTO().getGoodsPrice()+"小于25不到起送条件");
            }
            goodsSku.setGoodsName(" 25起送订单未满25元，无法配送！");
            cartSkuVO.setGoodsSku(goodsSku);
            notsupportMessage.add(cartSkuVO);
            tradeDTO.setNotSupportFreight(notsupportMessage);
        }
        if (tradeDTO.getPriceDetailDTO().getGoodsPrice() >= 59){
            tradeDTO.getPriceDetailDTO().setFreightPrice(0.00);
        }


    }

    /**
     * 特殊情况下对购物车金额进行护理
     *
     * @param tradeDTO
     */
    private void initPriceDTO(TradeDTO tradeDTO) {
        tradeDTO.getCartList().forEach(cartVO -> cartVO.setPriceDetailDTO(new PriceDetailDTO()));
        tradeDTO.setPriceDetailDTO(new PriceDetailDTO());
    }

    /**
     * 购物车价格
     *
     * @param tradeDTO 购物车展示信息
     */
    void buildCartPrice(TradeDTO tradeDTO) {
        //购物车列表
        List<CartVO> cartVOS = tradeDTO.getCartList();

        cartVOS.forEach(cartVO -> {

            cartVO.getPriceDetailDTO().accumulationPriceDTO(
                    cartVO.getCheckedSkuList().stream().filter(CartSkuVO::getChecked)
                            .map(CartSkuVO::getPriceDetailDTO).collect(Collectors.toList())
            );
            List<Integer> skuNum = cartVO.getSkuList().stream().filter(CartSkuVO::getChecked)
                    .map(CartSkuVO::getNum).collect(Collectors.toList());
            for (Integer num : skuNum) {
                cartVO.addGoodsNum(num);
            }
        });
    }


    /**
     * 初始化购物车
     *
     * @param tradeDTO 购物车展示信息
     */
    void buildTradePrice(TradeDTO tradeDTO) {
        tradeDTO.getPriceDetailDTO().accumulationPriceDTO(
                tradeDTO.getCartList().stream().map(CartVO::getPriceDetailDTO).collect(Collectors.toList()));
    }

}
