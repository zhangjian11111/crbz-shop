package cn.crbz.event.impl;

import cn.crbz.event.OrderStatusChangeEvent;
import cn.crbz.event.TradeEvent;
import cn.crbz.modules.order.cart.entity.dto.TradeDTO;
import cn.crbz.modules.order.order.entity.dto.OrderMessage;
import cn.crbz.modules.order.order.entity.vo.OrderVO;
import cn.crbz.modules.wechat.util.WechatMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信消息执行器
 *
 * @author Chopper
 * @version v1.0
 * 2021-04-19 14:25
 */
@Slf4j
@Service
public class WechatMessageExecute implements OrderStatusChangeEvent, TradeEvent {

    @Autowired
    private WechatMessageUtil wechatMessageUtil;

    @Override
    public void orderCreate(TradeDTO tradeDTO) {
        for (OrderVO orderVO : tradeDTO.getOrderVO()) {
            try {
                wechatMessageUtil.sendWechatMessage(orderVO.getSn());
            } catch (Exception e) {
                log.error("微信消息发送失败：" + orderVO.getSn(), e);
            }
        }
    }

    @Override
    public void orderChange(OrderMessage orderMessage) {

        switch (orderMessage.getNewStatus()) {
            case UNDELIVERED:
            case DELIVERED:
            case STAY_PICKED_UP:
            case COMPLETED:
                try {
                    wechatMessageUtil.sendWechatMessage(orderMessage.getOrderSn());
                } catch (Exception e) {
                    log.error("微信消息发送失败", e);
                }
                break;
            default:
                break;
        }

    }
}
