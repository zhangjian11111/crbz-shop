package cn.crbz.timetask.handler.impl.promotion;

import cn.crbz.modules.promotion.service.MemberCouponSignService;
import cn.crbz.timetask.handler.EveryDayExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 促销活动每日定时器
 *
 * @author Chopper
 * @since 2021/3/18 3:23 下午
 */
@Slf4j
@Component
public class MemberCouponSignEverydayExecute implements EveryDayExecute {

    @Autowired
    private MemberCouponSignService memberCouponSignService;

    /**
     * 将已过期的促销活动置为结束
     */
    @Override
    public void execute() {
        try {
            memberCouponSignService.clean();
        } catch (Exception e) {
            log.error("清除领取优惠券标记异常", e);
        }

    }

}
