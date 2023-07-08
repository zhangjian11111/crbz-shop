package cn.crbz.event.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import cn.crbz.event.AfterSaleStatusChangeEvent;
import cn.crbz.event.OrderStatusChangeEvent;
import cn.crbz.modules.distribution.entity.enums.DistributionOrderStatusEnum;
import cn.crbz.modules.distribution.service.DistributionOrderService;
import cn.crbz.modules.order.aftersale.entity.dos.AfterSale;
import cn.crbz.modules.order.order.entity.dto.OrderMessage;
import cn.crbz.modules.order.trade.entity.enums.AfterSaleStatusEnum;
import cn.crbz.modules.system.entity.dos.Setting;
import cn.crbz.modules.system.entity.dto.DistributionSetting;
import cn.crbz.modules.system.entity.enums.SettingEnum;
import cn.crbz.modules.system.service.SettingService;
import cn.crbz.timetask.handler.EveryDayExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分销订单入库
 *
 * @author Chopper
 * @since 2020-07-03 11:20
 */
@Slf4j
@Service
public class DistributionOrderExecute implements OrderStatusChangeEvent, EveryDayExecute, AfterSaleStatusChangeEvent {

    /**
     * 分销订单
     */
    @Autowired
    private DistributionOrderService distributionOrderService;

    @Autowired
    private SettingService settingService;


    @Override
    public void orderChange(OrderMessage orderMessage) {

        switch (orderMessage.getNewStatus()) {
            //订单带校验/订单代发货/待自提，则记录分销信息
            case TAKE:
            case STAY_PICKED_UP:
            case UNDELIVERED: {
                //记录分销订单
                distributionOrderService.calculationDistribution(orderMessage.getOrderSn());
                break;
            }
            case CANCELLED: {
                //修改分销订单状态
                distributionOrderService.cancelOrder(orderMessage.getOrderSn());
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void execute() {
        log.info("分销订单定时开始执行");
        //设置结算天数(解冻日期)
        Setting setting = settingService.get(SettingEnum.DISTRIBUTION_SETTING.name());
        DistributionSetting distributionSetting = JSONUtil.toBean(setting.getSettingValue(), DistributionSetting.class);
        //解冻时间
        DateTime dateTime = new DateTime();
        //当前时间-结算天数=最终结算时间
        dateTime = dateTime.offsetNew(DateField.DAY_OF_MONTH, -distributionSetting.getCashDay());
        //分销人员订单结算
        distributionOrderService.updateRebate(dateTime,DistributionOrderStatusEnum.WAIT_BILL.name());

    }

    @Override
    public void afterSaleStatusChange(AfterSale afterSale) {
        if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name())) {
            distributionOrderService.refundOrder(afterSale.getSn());
        }
    }

}
