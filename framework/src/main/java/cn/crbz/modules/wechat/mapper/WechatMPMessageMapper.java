package cn.crbz.modules.wechat.mapper;

import cn.crbz.modules.wechat.entity.dos.WechatMPMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * 微信小程序消息订阅 Dao层
 *
 * @author Chopper
 */
public interface WechatMPMessageMapper extends BaseMapper<WechatMPMessage> {

    /**
     * 删除微信服务消息
     */
    @Delete("delete from crbz_wechat_mp_message")
    void deleteAll();
}
