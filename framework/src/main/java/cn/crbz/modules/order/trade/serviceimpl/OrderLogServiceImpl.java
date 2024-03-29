package cn.crbz.modules.order.trade.serviceimpl;

import cn.crbz.modules.order.trade.entity.dos.OrderLog;
import cn.crbz.modules.order.trade.mapper.OrderLogMapper;
import cn.crbz.modules.order.trade.service.OrderLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单日志业务层实现
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements OrderLogService {

    @Override
    public List<OrderLog> getOrderLog(String orderSn) {
        LambdaQueryWrapper<OrderLog> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(OrderLog::getOrderSn, orderSn);
        return this.list(lambdaQueryWrapper);
    }
}
