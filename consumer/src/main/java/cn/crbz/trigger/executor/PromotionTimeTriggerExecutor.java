package cn.crbz.trigger.executor;

import cn.hutool.json.JSONUtil;
import cn.crbz.modules.order.order.service.OrderService;
import cn.crbz.modules.promotion.entity.dos.Pintuan;
import cn.crbz.modules.promotion.service.PintuanService;
import cn.crbz.trigger.TimeTriggerExecutor;
import cn.crbz.trigger.message.PintuanOrderMessage;
import cn.crbz.trigger.model.TimeExecuteConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 促销事件触发
 *
 * @author Chopper
 * @version v4.1
 * @since 2020/11/17 7:20 下午
 */
@Slf4j
@Component(TimeExecuteConstant.PROMOTION_EXECUTOR)
public class PromotionTimeTriggerExecutor implements TimeTriggerExecutor {
    /**
     * 订单
     */
    @Autowired
    private OrderService orderService;
    @Autowired
    private PintuanService pintuanService;


    @Override
    public void execute(Object object) {
        //拼团订单消息
        PintuanOrderMessage pintuanOrderMessage = JSONUtil.toBean(JSONUtil.parseObj(object), PintuanOrderMessage.class);
        if (pintuanOrderMessage != null && pintuanOrderMessage.getPintuanId() != null) {
            log.info("拼团订单信息消费：{}", pintuanOrderMessage);
            //拼团订单自动处理
            orderService.agglomeratePintuanOrder(pintuanOrderMessage.getPintuanId(), pintuanOrderMessage.getOrderSn());
        }
        Pintuan pintuan = pintuanService.getById(pintuanOrderMessage.getPintuanId());
        if (pintuan != null && pintuan.getId() != null) {
            this.orderService.checkFictitiousOrder(pintuan.getId(), pintuan.getRequiredNum(), pintuan.getFictitious());
        }
    }


}
