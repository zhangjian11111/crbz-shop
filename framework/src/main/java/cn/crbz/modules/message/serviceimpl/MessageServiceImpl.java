package cn.crbz.modules.message.serviceimpl;

import cn.crbz.common.properties.RocketmqCustomProperties;
import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.message.entity.dos.Message;
import cn.crbz.modules.message.entity.vos.MessageVO;
import cn.crbz.modules.message.mapper.MessageMapper;
import cn.crbz.modules.message.service.MessageService;
import cn.crbz.mybatis.util.PageUtil;
import cn.crbz.rocketmq.RocketmqSendCallbackBuilder;
import cn.crbz.rocketmq.tags.OtherTagsEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理端发送消息内容业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 3:48 下午
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;


    @Override
    public IPage<Message> getPage(MessageVO messageVO, PageVO pageVO) {
        return this.page(PageUtil.initPage(pageVO), messageVO.lambdaQueryWrapper());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean sendMessage(Message message) {
        //保存站内信信息
        this.save(message);
        //发送站内信消息提醒
        String noticeSendDestination = rocketmqCustomProperties.getNoticeSendTopic() + ":" + OtherTagsEnum.MESSAGE.name();
        rocketMQTemplate.asyncSend(noticeSendDestination, message, RocketmqSendCallbackBuilder.commonCallback());
        return true;
    }

    @Override
    public Boolean deleteMessage(String id) {
        //只有查询到此记录才真实删除，未找到记录则直接返回true即可
        Message message = this.getById(id);
        if (message != null) {
            return this.removeById(id);
        }
        return true;
    }
}
