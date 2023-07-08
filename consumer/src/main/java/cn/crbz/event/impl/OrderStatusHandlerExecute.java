package cn.crbz.event.impl;

import cn.crbz.event.TradeEvent;
import cn.crbz.modules.order.cart.entity.dto.TradeDTO;
import cn.crbz.modules.order.order.service.TradeService;
import cn.crbz.modules.payment.entity.enums.PaymentMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 订单状态处理类
 *
 * @author Chopper
 * @since 2020-07-03 11:20
 **/
@Service
public class OrderStatusHandlerExecute implements TradeEvent {


    @Autowired
    private TradeService tradeService;

    @Override
    public void orderCreate(TradeDTO tradeDTO) {
        //如果订单需要支付金额为0，则将订单步入到下一个流程
        if (tradeDTO.getPriceDetailDTO().getFlowPrice() <= 0) {
            tradeService.payTrade(tradeDTO.getSn(), PaymentMethodEnum.BANK_TRANSFER.name(), "-1");
        }

    }
}
