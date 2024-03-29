package cn.crbz.trigger.executor;

import cn.hutool.json.JSONUtil;
import cn.crbz.modules.promotion.service.CouponActivityService;
import cn.crbz.trigger.TimeTriggerExecutor;
import cn.crbz.trigger.message.CouponActivityMessage;
import cn.crbz.trigger.model.TimeExecuteConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 优惠券活动精准发圈延时触发
 *
 * @author Bulbasaur
 * @since 2021/6/1 5:02 下午
 */
@Slf4j
@Component(TimeExecuteConstant.COUPON_ACTIVITY_EXECUTOR)
public class CouponActivityTriggerExecutor implements TimeTriggerExecutor {


    @Autowired
    private CouponActivityService couponActivityService;

    @Override
    public void execute(Object object) {
        CouponActivityMessage couponActivityMessage = JSONUtil.toBean(JSONUtil.parseObj(object), CouponActivityMessage.class);
        couponActivityService.specifyCoupon(couponActivityMessage.getCouponActivityId());
    }
}
