package cn.crbz.event.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.crbz.cache.Cache;
import cn.crbz.cache.CachePrefix;
import cn.crbz.event.MemberRegisterEvent;
import cn.crbz.modules.member.entity.dos.Member;
import cn.crbz.modules.member.service.MemberService;
import cn.crbz.modules.promotion.entity.dto.CouponActivityTrigger;
import cn.crbz.modules.promotion.entity.enums.CouponActivityTypeEnum;
import cn.crbz.modules.promotion.service.CouponActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册赠券活动
 *
 * @author Bulbasaur
 * @since 2021/5/24 10:48 上午
 */
@Component
public class RegisteredCouponActivityExecute implements MemberRegisterEvent {

    @Autowired
    private CouponActivityService couponActivityService;


    @Autowired
    private MemberService memberService;
    @Autowired
    private Cache cache;

    /**
     * 获取进行中的注册赠券的优惠券活动
     * 发送注册赠券
     *
     * @param member 会员
     */
    @Override
    public void memberRegister(Member member) {
        //用户注册赠券
        couponActivityService.trigger(CouponActivityTrigger.builder()
                .nickName(member.getNickName())
                .userId(member.getId())
                .couponActivityTypeEnum(CouponActivityTypeEnum.REGISTERED)
                .build());
        //邀请人赠券
        String memberId = (String) cache.get(CachePrefix.INVITER.getPrefix() + member.getId());
        if (CharSequenceUtil.isNotEmpty(memberId)) {
            //邀请人
            Member inviter = memberService.getById(memberId);
            couponActivityService.trigger(CouponActivityTrigger.builder()
                    .nickName(inviter.getNickName())
                    .userId(inviter.getId())
                    .couponActivityTypeEnum(CouponActivityTypeEnum.INVITE_NEW)
                    .build());
        }
    }
}
