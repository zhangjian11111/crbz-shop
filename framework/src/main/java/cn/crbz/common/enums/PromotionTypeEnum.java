package cn.crbz.common.enums;

import cn.crbz.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * 促销分类枚举
 *
 * @author Chopper
 * @since 2021/2/1 19:32
 */

@Slf4j
public enum PromotionTypeEnum {
    /**
     * 促销枚举
     */
    PINTUAN("拼团"),
    SECKILL("秒杀"),
    COUPON("优惠券"),
    PLATFORM_COUPON("平台优惠券"),
    FULL_DISCOUNT("满减"),
    POINTS_GOODS("积分商品"),
    KANJIA("砍价"),
    COUPON_ACTIVITY("优惠券活动");

    /**
     * 有促销库存的活动类型
     */
    public static final PromotionTypeEnum[] haveStockPromotion = new PromotionTypeEnum[]{PINTUAN, SECKILL, KANJIA, POINTS_GOODS};

    /**
     * 有独立促销库存的活动类型
     */
    public static final PromotionTypeEnum[] haveIndependanceStockPromotion = new PromotionTypeEnum[]{SECKILL};

    private final String description;

    PromotionTypeEnum(String description) {
        this.description = description;
    }

    /**
     * 是否拥有库存
     */
    public static boolean haveStock(String promotionType) {
        for (PromotionTypeEnum promotionTypeEnum : haveStockPromotion) {
            if (promotionTypeEnum.name().equals(promotionType)) {
                return true;
            }
        }
        return false;
    }

    public String description() {
        return description;
    }


    /**
     * 判断促销类型是否有效
     * @param typeEnumValue
     * @return
     */
    public static boolean isValid(String typeEnumValue) {
        if (StringUtils.isBlank(typeEnumValue)) {
            return false;
        }
        return Arrays.stream(PromotionTypeEnum.values()).anyMatch(c -> c.name().equals(typeEnumValue));
    }

    /**
     * 判断订单类型是否可售后
     * POINTS\KANJIA 两种促销类型的订单不可进行售后
     * @return true 可售后 false 不可售后
     */
    public static boolean isCanAfterSale(String promotionType) {
        if (!isValid(promotionType)) {
            return true;
        }
        EnumSet<PromotionTypeEnum> noAfterSale = EnumSet.of(PromotionTypeEnum.KANJIA, PromotionTypeEnum.POINTS_GOODS);
        return !noAfterSale.contains(PromotionTypeEnum.valueOf(promotionType));
    }

}
