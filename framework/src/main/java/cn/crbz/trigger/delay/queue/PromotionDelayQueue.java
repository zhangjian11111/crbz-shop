package cn.crbz.trigger.delay.queue;

import cn.crbz.trigger.delay.AbstractDelayQueueMachineFactory;
import cn.crbz.trigger.enums.DelayQueueEnums;
import org.springframework.stereotype.Component;

/**
 * 促销延迟队列
 *
 * @author paulG
 * @version v4.1
 * @since 2020/11/17 7:19 下午
 * @since 1
 */
@Component
public class PromotionDelayQueue extends AbstractDelayQueueMachineFactory {


    @Override
    public String setDelayQueueName() {
        return DelayQueueEnums.PROMOTION.name();
    }
}
