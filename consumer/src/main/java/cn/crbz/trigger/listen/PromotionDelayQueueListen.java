package cn.crbz.trigger.listen;

import cn.hutool.json.JSONUtil;
import cn.crbz.trigger.AbstractDelayQueueListen;
import cn.crbz.trigger.enums.DelayQueueEnums;
import cn.crbz.trigger.interfaces.TimeTrigger;
import cn.crbz.trigger.model.TimeTriggerMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 * PromotionTimeTriggerListen
 *
 * @author Chopper
 * @version v1.0
 * 2021-06-11 10:47
 */
@Component
public class PromotionDelayQueueListen extends AbstractDelayQueueListen {

    @Autowired
    private TimeTrigger timeTrigger;

    @Override
    public void invoke(String jobId) {
        timeTrigger.execute(JSONUtil.toBean(jobId, TimeTriggerMsg.class));
    }


    @Override
    public String setDelayQueueName() {
        return DelayQueueEnums.PROMOTION.name();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.init();
    }
}
