package cn.crbz.modules.promotion.service;

import cn.crbz.modules.promotion.entity.dos.CouponActivity;
import cn.crbz.modules.promotion.entity.dos.MemberCoupon;
import cn.crbz.modules.promotion.entity.dto.CouponActivityTrigger;
import cn.crbz.modules.promotion.entity.vos.CouponActivityVO;

import java.util.List;

/**
 * 优惠券活动业务层
 *
 * @author Bulbasaur
 * @since 2021/5/20 6:10 下午
 */
public interface CouponActivityService extends AbstractPromotionsService<CouponActivity> {


    /**
     * 获取优惠券活动VO
     * 包含优惠券活动信息以及优惠券关联优惠券列表
     *
     * @param couponActivityId 优惠券活动ID
     * @return 优惠券VO
     */
    CouponActivityVO getCouponActivityVO(String couponActivityId);

    /**
     * 精准发券
     *
     * @param couponActivity 精准发券
     */
    void specify(CouponActivity couponActivity);

    /**
     * 用户优惠券活动触发
     *
     * @return 优惠券列表
     */
    List<MemberCoupon> trigger(CouponActivityTrigger couponActivityTrigger);

}
