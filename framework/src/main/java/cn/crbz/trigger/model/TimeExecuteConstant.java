package cn.crbz.trigger.model;

/**
 * 延时任务执行器常量
 *
 * @author paulG
 * @since 2020/8/20
 **/
public interface TimeExecuteConstant {

    /**
     * 促销延迟加载执行器
     */
    public static final String PROMOTION_EXECUTOR = "promotionTimeTriggerExecutor";

    /**
     * 直播间延迟加载执行器
     */
    public static final String BROADCAST_EXECUTOR = "broadcastTimeTriggerExecutor";

    /**
     * 促销延迟加载执行器
     */
    public static final String COUPON_ACTIVITY_EXECUTOR = "couponActivityExecutor";
}
