package cn.crbz.event.impl;


import cn.hutool.core.text.CharSequenceUtil;
import cn.crbz.common.utils.CurrencyUtil;
import cn.crbz.event.AfterSaleStatusChangeEvent;
import cn.crbz.event.GoodsCommentCompleteEvent;
import cn.crbz.event.MemberRegisterEvent;
import cn.crbz.event.OrderStatusChangeEvent;
import cn.crbz.modules.member.entity.dos.Member;
import cn.crbz.modules.member.entity.dos.MemberEvaluation;
import cn.crbz.modules.member.entity.enums.PointTypeEnum;
import cn.crbz.modules.member.service.MemberService;
import cn.crbz.modules.order.aftersale.entity.dos.AfterSale;
import cn.crbz.modules.order.order.entity.dos.Order;
import cn.crbz.modules.order.order.entity.dto.OrderMessage;
import cn.crbz.modules.order.order.entity.enums.OrderPromotionTypeEnum;
import cn.crbz.modules.order.order.entity.enums.OrderStatusEnum;
import cn.crbz.modules.order.order.entity.enums.PayStatusEnum;
import cn.crbz.modules.order.order.service.OrderService;
import cn.crbz.modules.order.trade.entity.enums.AfterSaleStatusEnum;
import cn.crbz.modules.system.entity.dos.Setting;
import cn.crbz.modules.system.entity.dto.PointSetting;
import cn.crbz.modules.system.entity.enums.SettingEnum;
import cn.crbz.modules.system.service.SettingService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员积分
 *
 * @author Bulbasaur
 * @since 2020-07-03 11:20
 */
@Service
public class MemberPointExecute implements MemberRegisterEvent, GoodsCommentCompleteEvent, OrderStatusChangeEvent, AfterSaleStatusChangeEvent {

    /**
     * 配置
     */
    @Autowired
    private SettingService settingService;
    /**
     * 会员
     */
    @Autowired
    private MemberService memberService;
    /**
     * 订单
     */
    @Autowired
    private OrderService orderService;

    /**
     * 会员注册赠送积分
     *
     * @param member 会员
     */
    @Override
    public void memberRegister(Member member) {
        //获取积分设置
        PointSetting pointSetting = getPointSetting();
        //赠送会员积分
        memberService.updateMemberPoint(pointSetting.getRegister().longValue(), PointTypeEnum.INCREASE.name(), member.getId(), "会员注册，赠送积分" + pointSetting.getRegister() + "分");
    }

    /**
     * 会员评价赠送积分
     *
     * @param memberEvaluation 会员评价
     */
    @Override
    public void goodsComment(MemberEvaluation memberEvaluation) {
        //获取积分设置
        PointSetting pointSetting = getPointSetting();
        //赠送会员积分
        memberService.updateMemberPoint(pointSetting.getComment().longValue(), PointTypeEnum.INCREASE.name(), memberEvaluation.getMemberId(), "会员评价，赠送积分" + pointSetting.getComment() + "分");
    }

    /**
     * 非积分订单订单完成后赠送积分
     *
     * @param orderMessage 订单消息
     */
    @Override
    public void orderChange(OrderMessage orderMessage) {

        switch (orderMessage.getNewStatus()) {
            case CANCELLED: {
                Order order = orderService.getBySn(orderMessage.getOrderSn());
                Long point = order.getPriceDetailDTO().getPayPoint();
                if (point <= 0) {
                    return;
                }
                //如果未付款，则不去要退回相关代码执行
                if (order.getPayStatus().equals(PayStatusEnum.UNPAID.name())) {
                    return;
                }
                String content = "订单取消，积分返还：" + point + "分";
                //赠送会员积分
                memberService.updateMemberPoint(point, PointTypeEnum.INCREASE.name(), order.getMemberId(), content);
                break;
            }
            case COMPLETED: {
                Order order = orderService.getBySn(orderMessage.getOrderSn());
                //如果是积分订单 则直接返回
                if (CharSequenceUtil.isNotEmpty(order.getOrderPromotionType())
                        && order.getOrderPromotionType().equals(OrderPromotionTypeEnum.POINTS.name())) {
                    return;
                }
                //获取积分设置
                PointSetting pointSetting = getPointSetting();
                if (pointSetting.getConsumer() == 0) {
                    return;
                }
                //计算赠送积分数量
                Double point = CurrencyUtil.mul(pointSetting.getConsumer(), order.getFlowPrice(), 0);
                //赠送会员积分
                memberService.updateMemberPoint(point.longValue(), PointTypeEnum.INCREASE.name(), order.getMemberId(), "会员下单，赠送积分" + point + "分");
                break;
            }

            default:
                break;
        }
    }


    /**
     * 提交售后后扣除积分
     *
     * @param afterSale 售后
     */
    @Override
    public void afterSaleStatusChange(AfterSale afterSale) {
        if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name())) {
            Order order = orderService.getBySn(afterSale.getOrderSn());
            //获取积分设置
            PointSetting pointSetting = getPointSetting();
            if (pointSetting.getConsumer() == 0 || !OrderStatusEnum.COMPLETED.name().equals(order.getOrderStatus())) {
                return;
            }
            //计算扣除积分数量
            Double point = CurrencyUtil.mul(pointSetting.getConsumer(), afterSale.getActualRefundPrice(), 0);
            //扣除会员积分
            memberService.updateMemberPoint(point.longValue(), PointTypeEnum.REDUCE.name(), afterSale.getMemberId(), "会员退款，回退消费赠送积分" + point + "分");

        }
    }

    /**
     * 获取积分设置
     *
     * @return 积分设置
     */
    private PointSetting getPointSetting() {
        Setting setting = settingService.get(SettingEnum.POINT_SETTING.name());
        return new Gson().fromJson(setting.getSettingValue(), PointSetting.class);
    }
}
