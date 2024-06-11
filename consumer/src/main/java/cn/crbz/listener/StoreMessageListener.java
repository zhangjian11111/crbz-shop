package cn.crbz.listener;

import cn.hutool.json.JSONUtil;
import cn.crbz.event.MemberRegisterEvent;
import cn.crbz.event.StoreSettingChangeEvent;
import cn.crbz.modules.member.entity.dos.Member;
import cn.crbz.modules.store.entity.dos.Store;
import cn.crbz.rocketmq.tags.StoreTagsEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 店铺消息
 * @author chc
 * @since 2022/6/2114:46
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "${crbz.data.rocketmq.store-topic}", consumerGroup = "${crbz.data.rocketmq.store-group}")
public class StoreMessageListener implements RocketMQListener<MessageExt> {
    @Autowired
    private List<StoreSettingChangeEvent> storeSettingChangeEventList;

    @Override
    public void onMessage(MessageExt messageExt) {
        switch (StoreTagsEnum.valueOf(messageExt.getTags())){
            //修改店铺
            case EDIT_STORE_SETTING:
                for (StoreSettingChangeEvent storeSettingChangeEvent : storeSettingChangeEventList) {
                    try {
                        Store store = JSONUtil.toBean(new String(messageExt.getBody()), Store.class);
                        storeSettingChangeEvent.storeSettingChange(store);
                    } catch (Exception e) {
                        log.error("会员{},在{}业务中，状态修改事件执行异常",
                                new String(messageExt.getBody()),
                                storeSettingChangeEvent.getClass().getName(),
                                e);
                    }
                }
                break;
            default:
                break;
        }
    }
}
