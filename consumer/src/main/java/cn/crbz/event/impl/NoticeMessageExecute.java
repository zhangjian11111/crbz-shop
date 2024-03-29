package cn.crbz.event.impl;

import cn.crbz.event.*;
import cn.crbz.modules.member.entity.dto.MemberPointMessage;
import cn.crbz.modules.member.entity.enums.PointTypeEnum;
import cn.crbz.modules.message.entity.dto.NoticeMessageDTO;
import cn.crbz.modules.message.entity.enums.NoticeMessageNodeEnum;
import cn.crbz.modules.message.entity.enums.NoticeMessageParameterEnum;
import cn.crbz.modules.message.service.NoticeMessageService;
import cn.crbz.modules.order.aftersale.entity.dos.AfterSale;
import cn.crbz.modules.order.cart.entity.dto.TradeDTO;
import cn.crbz.modules.order.order.entity.dto.OrderMessage;
import cn.crbz.modules.order.order.entity.enums.OrderPromotionTypeEnum;
import cn.crbz.modules.order.order.entity.vo.OrderDetailVO;
import cn.crbz.modules.order.order.service.OrderService;
import cn.crbz.modules.order.trade.entity.enums.AfterSaleStatusEnum;
import cn.crbz.modules.order.trade.entity.enums.AfterSaleTypeEnum;
import cn.crbz.modules.wallet.entity.dto.MemberWithdrawalMessage;
import cn.crbz.modules.wallet.entity.enums.WithdrawStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * 通知类消息实现
 *
 * @author Chopper
 * @since 2020-07-03 11:20
 **/
@Service
@Slf4j
public class NoticeMessageExecute implements TradeEvent, OrderStatusChangeEvent, AfterSaleStatusChangeEvent, MemberPointChangeEvent, MemberWithdrawalEvent {

    @Autowired
    private NoticeMessageService noticeMessageService;
    @Autowired
    private OrderService orderService;


    @Override
    public void orderCreate(TradeDTO tradeDTO) {
        //订单创建发送订单创建站内信息
        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
        noticeMessageDTO.setMemberId(tradeDTO.getMemberId());
        noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.ORDER_CREATE_SUCCESS);
        Map<String, String> params = new HashMap<>(2);
        params.put("goods", tradeDTO.getSkuList().get(0).getGoodsSku().getGoodsName());
        noticeMessageDTO.setParameter(params);
        //保存站内信
        noticeMessageService.noticeMessage(noticeMessageDTO);
    }

    @Override
    public void orderChange(OrderMessage orderMessage) {
        log.info("尼玛订单状态变成啥了：：："+orderMessage);

        //查询订单信息
        OrderDetailVO orderDetailVO = orderService.queryDetail(orderMessage.getOrderSn());
        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
        //如果订单状态不为空
        if (orderDetailVO != null && orderDetailVO.getOrderItems() != null && !orderDetailVO.getOrderItems().isEmpty()) {
            Map<String, String> params = new HashMap<>(2);
            switch (orderMessage.getNewStatus()) {
                //如果订单新的状态为已取消 则发送取消订单站内信
                case CANCELLED:
                    params.put(NoticeMessageParameterEnum.CANCEL_REASON.getType(), orderDetailVO.getOrder().getCancelReason());
                    noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.ORDER_CANCEL_SUCCESS);
                    break;
                //如果订单新的状态为已经支付，则发送支付成功站内信
                case PAID:
                    noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.ORDER_PAY_SUCCESS);
                    break;
                //如果订单新的状态为已发货，则发送已发货站内信
                case DELIVERED:
                    noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.ORDER_DELIVER);
                    break;
                //如果订单新的状态为已完成，则发送已完成站内信
                case COMPLETED:
                    //订单完成消息
                    noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.ORDER_COMPLETE);
                    //订单完成也可以进行评价，所以要有评价消息
                    noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.ORDER_EVALUATION);
                    break;
                //如果是拼团订单，发送拼团成功消息
                case UNDELIVERED:
                    if (OrderPromotionTypeEnum.PINTUAN.name().equals(orderDetailVO.getOrder().getOrderPromotionType())) {
                        //拼团成功消息
                        noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.PINTUAN_SUCCESS);
                    }
                    break;
                default:
                    break;
            }
            noticeMessageDTO.setMemberId(orderDetailVO.getOrder().getMemberId());
            //添加站内信参数
            params.put(NoticeMessageParameterEnum.GOODS.getType(), orderDetailVO.getOrderItems().get(0).getGoodsName());
            noticeMessageDTO.setParameter(params);

            //如果有消息，则发送消息
            if (noticeMessageDTO.getNoticeMessageNodeEnum() != null) {
                //保存站内信
                noticeMessageService.noticeMessage(noticeMessageDTO);
            }
        }
    }

    @Override
    public void afterSaleStatusChange(AfterSale afterSale) {
        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
        noticeMessageDTO.setMemberId(afterSale.getMemberId());
        Map<String, String> params = new HashMap<>(2);
        params.put("goods", afterSale.getGoodsName());
        params.put("refuse", afterSale.getAuditRemark());
        noticeMessageDTO.setParameter(params);
        //如果售后单是申请中 则发送申请中站内信
        if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.APPLY.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.AFTER_SALE_CREATE_SUCCESS);
        }
        //售后审核同意切退货站内信通知
        else if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.PASS.name()) && afterSale.getServiceType().equals(AfterSaleTypeEnum.RETURN_GOODS.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.RETURN_GOODS_PASS);
        }
        //售后审核拒绝且退货站内信通知
        else if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.REFUSE.name()) && afterSale.getServiceType().equals(AfterSaleTypeEnum.RETURN_GOODS.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.RETURN_GOODS_REFUSE);
        }
        //售后审核同意切退款站内信通知
        else if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.PASS.name()) && afterSale.getServiceType().equals(AfterSaleTypeEnum.RETURN_MONEY.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.RETURN_MONEY_PASS);
        }
        //售后审核拒绝且退款站内信通知
        else if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.REFUSE.name()) && afterSale.getServiceType().equals(AfterSaleTypeEnum.RETURN_MONEY.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.RETURN_MONEY_REFUSE);
        }
        //售后商家确认收货站内信通知
        else if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.SELLER_CONFIRM.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.AFTER_SALE_ROG_PASS);
        }
        //退货物品拒收站内信通知
        else if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.SELLER_TERMINATION.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.AFTER_SALE_ROG_REFUSE);
        }
        //售后完成通知
        else if (afterSale.getServiceStatus().equals(AfterSaleStatusEnum.COMPLETE.name())) {
            noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.AFTER_SALE_COMPLETE);
        }
        //保存站内信
        if (noticeMessageDTO.getNoticeMessageNodeEnum() != null) {
            noticeMessageService.noticeMessage(noticeMessageDTO);
        }

    }

    @Override
    public void memberPointChange(MemberPointMessage memberPointMessage) {
        if (memberPointMessage == null) {
            return;
        }
        //组织站内信参数
        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
        noticeMessageDTO.setMemberId(memberPointMessage.getMemberId());
        Map<String, String> params = new HashMap<>(2);
        if (memberPointMessage.getType().equals(PointTypeEnum.INCREASE.name())) {
            params.put("expenditure_points", "0");
            params.put("income_points", memberPointMessage.getPoint().toString());
        } else {
            params.put("expenditure_points", memberPointMessage.getPoint().toString());
            params.put("income_points", "0");
        }
        noticeMessageDTO.setParameter(params);
        noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.POINT_CHANGE);
        //发送站内通知信息
        noticeMessageService.noticeMessage(noticeMessageDTO);
    }


    @Override
    public void memberWithdrawal(MemberWithdrawalMessage memberWithdrawalMessage) {
        NoticeMessageDTO noticeMessageDTO = new NoticeMessageDTO();
        noticeMessageDTO.setMemberId(memberWithdrawalMessage.getMemberId());
        Map<String, String> params = new HashMap<>(2);
        switch (WithdrawStatusEnum.valueOf(memberWithdrawalMessage.getStatus())) {
            case APPLY:
                //如果提现状态为申请则发送申请提现站内消息
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_CREATE);
                break;
            case FAIL_AUDITING:
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_AUDIT_ERROR);
                break;
            case SUCCESS:
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_SUCCESS);
                break;
            case ERROR:
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_ERROR);
                break;
            case VIA_AUDITING:
                noticeMessageDTO.setNoticeMessageNodeEnum(NoticeMessageNodeEnum.WALLET_WITHDRAWAL_AUDIT_SUCCESS);
            default:
                break;
        }

        params.put("price", memberWithdrawalMessage.getPrice().toString());
        noticeMessageDTO.setParameter(params);
        //发送提现申请消息
        noticeMessageService.noticeMessage(noticeMessageDTO);
    }
}
