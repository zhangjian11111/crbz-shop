package cn.lili.modules.order.cart.render.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.utils.CurrencyUtil;
import cn.lili.modules.member.entity.dos.MemberAddress;
import cn.lili.modules.order.cart.entity.dto.TradeDTO;
import cn.lili.modules.order.cart.entity.enums.RenderStepEnums;
import cn.lili.modules.order.cart.entity.vo.CartSkuVO;
import cn.lili.modules.order.cart.render.CartRenderStep;
import cn.lili.modules.store.entity.dos.FreightTemplateChild;
import cn.lili.modules.store.entity.dto.FreightTemplateChildDTO;
import cn.lili.modules.store.entity.enums.FreightTemplateEnum;
import cn.lili.modules.store.entity.vos.FreightTemplateVO;
import cn.lili.modules.store.service.FreightTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * sku 运费计算
 *
 * @author Chopper
 * @since 2020-07-02 14:47
 */
@Service
@Slf4j
public class SkuFreightRender implements CartRenderStep {

    @Autowired
    private FreightTemplateService freightTemplateService;

    @Override
    public RenderStepEnums step() {
        return RenderStepEnums.SKU_FREIGHT;
    }

    @Override
    public void render(TradeDTO tradeDTO) {
        log.info("运费计算运费计算：：！！！");
        List<CartSkuVO> cartSkuVOS = tradeDTO.getCheckedSkuList();
        //会员收货地址问题处理
        MemberAddress memberAddress = tradeDTO.getMemberAddress();
        //如果收货地址为空，则抛出异常
        if (memberAddress == null) {
            return;
        }

        //计算距离
        int distance = 0;
        int counting = 0;
        int distanceNum = 2;
        ArrayList<String> list = new ArrayList<>();
        String gdMapUrl = "https://restapi.amap.com/v3/distance?key=2ffb3bf9fe600c584a6fd6b64b7d4791&origins=125.393144,43.832683&destination="+memberAddress.getLon()+","+memberAddress.getLat()+"&type=1";
        log.info(gdMapUrl);
        String content = HttpUtil.get(gdMapUrl);
        JSONObject distanceObject = JSONUtil.parseObj(content);
        String resultsObject = distanceObject.getStr("results");
        for (String retval: resultsObject.split("\\[|:|\\{|]|,|\"|}")){
            log.info("分割的字符串："+retval);
            list.add(retval);
            counting++;
            if (retval.equals("distance")){
                distanceNum += counting;
            }
            log.info("距离在这里：：："+counting+"    ::"+distanceNum);
        }
        log.info("list里存的数据：：："+list+"长度：：："+list.size());
        try {
            distance = Integer.parseInt(list.get(distanceNum));
        }catch (ServiceException se) {
            log.error(ResultCode.DISTANCE_CHANGE.message(), se);
            throw new ServiceException(ResultCode.DISTANCE_CHANGE);
        }
        log.info(content);
        log.info("距离距离：：："+distance);


        //运费分组信息
        Map<String, List<String>> freightGroups = freightTemplateGrouping(cartSkuVOS);

        //循环运费模版
        for (Map.Entry<String, List<String>> freightTemplateGroup : freightGroups.entrySet()) {
            log.info("运费模板key：："+freightTemplateGroup.getKey());
            log.info("运费模板value：："+freightTemplateGroup.getValue());

            //商品id列表
            List<String> skuIds = freightTemplateGroup.getValue();

            //当前购物车商品列表
            List<CartSkuVO> currentCartSkus = cartSkuVOS.stream().filter(item -> skuIds.contains(item.getGoodsSku().getId())).collect(Collectors.toList());

            //寻找对应对商品运费计算模版
            FreightTemplateVO freightTemplate = freightTemplateService.getFreightTemplate(freightTemplateGroup.getKey());
            if (freightTemplate != null
                    && freightTemplate.getFreightTemplateChildList() != null
                    && !freightTemplate.getFreightTemplateChildList().isEmpty()) {
                //店铺模版免运费则跳过
                log.info("运费模板类型："+freightTemplate.getPricingMethod());
                if (freightTemplate.getPricingMethod().equals(FreightTemplateEnum.FREE.name())) {
                    break;
                }

                //运费模版
                FreightTemplateChild freightTemplateChild = null;

                //获取市级别id匹配运费模版
                String addressId = memberAddress.getConsigneeAddressIdPath().split(",")[1];
                for (FreightTemplateChild templateChild : freightTemplate.getFreightTemplateChildList()) {
                    //模版匹配判定
                    if (templateChild.getAreaId().contains(addressId)) {
                        freightTemplateChild = templateChild;
                        break;
                    }
                }
                //如果没有匹配到物流规则，则说明不支持配送
                if (freightTemplateChild == null) {
                    if (tradeDTO.getNotSupportFreight() == null) {
                        tradeDTO.setNotSupportFreight(new ArrayList<>());
                    }
                    tradeDTO.getNotSupportFreight().addAll(currentCartSkus);
                    continue;
                }

                //物流规则模型创立
                FreightTemplateChildDTO freightTemplateChildDTO = new FreightTemplateChildDTO(freightTemplateChild);
                //模型写入运费模版设置的计费方式
                freightTemplateChildDTO.setPricingMethod(freightTemplate.getPricingMethod());

                //计算运费总数
                Double count = currentCartSkus.stream().mapToDouble(item ->
                        // 根据计费规则 累加计费基数
                        freightTemplateChildDTO.getPricingMethod().equals(FreightTemplateEnum.NUM.name()) ?
                                item.getNum().doubleValue() :
                                CurrencyUtil.mul(item.getNum(), item.getGoodsSku().getWeight())
                ).sum();
                if (freightTemplateChildDTO.getPricingMethod().equals(FreightTemplateEnum.DISTANCE.name())){
                    count = Double.valueOf(distance)/1000;
                }
                log.info("计数（件数/重量/距离："+count);

                //计算运费
                Double countFreight = countFreight(count, freightTemplateChildDTO);

                //写入SKU运费
                resetFreightPrice(FreightTemplateEnum.valueOf(freightTemplateChildDTO.getPricingMethod()), count, countFreight, currentCartSkus);
            }
        }
    }


    /**
     * sku运费写入
     *
     * @param freightTemplateEnum 运费计算模式
     * @param count               计费基数总数
     * @param countFreight        总运费
     * @param cartSkuVOS          与运费相关的购物车商品
     */
    private void resetFreightPrice(FreightTemplateEnum freightTemplateEnum, Double count, Double countFreight, List<CartSkuVO> cartSkuVOS) {

        //剩余运费 默认等于总运费
        Double surplusFreightPrice = countFreight;

        //当前下标
        int index = 1;
        for (CartSkuVO cartSkuVO : cartSkuVOS) {
            //如果是最后一个 则将剩余运费直接赋值
            //PS: 循环中避免百分比累加不等于100%，所以最后一个运费不以比例计算，直接将剩余运费赋值
            if (index == cartSkuVOS.size()) {
                cartSkuVO.getPriceDetailDTO().setFreightPrice(surplusFreightPrice);
                break;
            }

            Double freightPrice = freightTemplateEnum == FreightTemplateEnum.NUM ?
                    CurrencyUtil.mul(countFreight, CurrencyUtil.div(cartSkuVO.getNum(), count)) :
                    CurrencyUtil.mul(countFreight,
                            CurrencyUtil.div(CurrencyUtil.mul(cartSkuVO.getNum(), cartSkuVO.getGoodsSku().getWeight()), count));

            //剩余运费=总运费-当前循环的商品运费
            surplusFreightPrice = CurrencyUtil.sub(surplusFreightPrice, freightPrice);

            cartSkuVO.getPriceDetailDTO().setFreightPrice(freightPrice);
            index++;
        }
    }

    /**
     * 运费模版分组
     *
     * @param cartSkuVOS 购物车商品
     * @return map<运费模版id ， List < skuid>>
     */
    private Map<String, List<String>> freightTemplateGrouping(List<CartSkuVO> cartSkuVOS) {
        Map<String, List<String>> map = new HashMap<>();
        //循环渲染购物车商品运费价格
        for (CartSkuVO cartSkuVO : cartSkuVOS) {
            ////免运费判定
            String freightTemplateId = cartSkuVO.getGoodsSku().getFreightTemplateId();
            if (Boolean.TRUE.equals(cartSkuVO.getIsFreeFreight()) || freightTemplateId == null) {
                continue;
            }
            //包含 则value值中写入sku标识，否则直接写入新的对象，key为模版id，value为new arraylist
            if (map.containsKey(freightTemplateId)) {
                map.get(freightTemplateId).add(cartSkuVO.getGoodsSku().getId());
            } else {
                List<String> skuIdsList = new ArrayList<>();
                skuIdsList.add(cartSkuVO.getGoodsSku().getId());
                map.put(freightTemplateId, skuIdsList);
            }
        }
        return map;
    }


    /**
     * 计算运费
     *
     * @param count    重量/件
     * @param template 计算模版
     * @return 运费
     */
    private Double countFreight(Double count, FreightTemplateChildDTO template) {
        try {
            Double finalFreight = template.getFirstPrice();
            //不满首重 / 首件
            if (template.getFirstCompany() >= count) {
                return finalFreight;
            }
            //如果续重/续件，费用不为空，则返回
            if (template.getContinuedCompany() == 0 || template.getContinuedPrice() == 0) {
                return finalFreight;
            }

            //计算 续重 / 续件 价格
            Double continuedCount = count - template.getFirstCompany();
            return CurrencyUtil.add(finalFreight,
                    CurrencyUtil.mul(Math.ceil(continuedCount / template.getContinuedCompany()), template.getContinuedPrice()));
        } catch (Exception e) {
            return 0D;
        }


    }


}
