package cn.crbz.modules.sms.serviceimpl;

import cn.hutool.json.JSONUtil;
import cn.crbz.common.properties.RocketmqCustomProperties;
import cn.crbz.common.utils.BeanUtil;
import cn.crbz.modules.sms.entity.dos.SmsReach;
import cn.crbz.modules.sms.entity.dto.SmsReachDTO;
import cn.crbz.modules.sms.mapper.SmsReachMapper;
import cn.crbz.modules.sms.service.SmsReachService;
import cn.crbz.rocketmq.RocketmqSendCallbackBuilder;
import cn.crbz.rocketmq.tags.OtherTagsEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 短信任务业务层实现
 *
 * @author Bulbasaur
 * @since 2021/1/30 3:19 下午
 */
@Service
public class SmsReachServiceImpl extends ServiceImpl<SmsReachMapper, SmsReach> implements SmsReachService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSmsReach(SmsReach smsReach,List<String> mobile) {
        String destination = rocketmqCustomProperties.getNoticeSendTopic() + ":" + OtherTagsEnum.SMS.name();
        SmsReachDTO smsReachDTO = new SmsReachDTO();
        BeanUtil.copyProperties(smsReach,smsReachDTO);
        smsReachDTO.setMobile(mobile);
        this.save(smsReach);
        //发送短信批量发送mq消息
        rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(smsReachDTO), RocketmqSendCallbackBuilder.commonCallback());

    }
}
