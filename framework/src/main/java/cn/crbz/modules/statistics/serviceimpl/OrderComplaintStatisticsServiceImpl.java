package cn.crbz.modules.statistics.serviceimpl;

import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.security.enums.UserEnums;
import cn.crbz.common.utils.StringUtils;
import cn.crbz.modules.order.aftersale.entity.enums.ComplaintStatusEnum;
import cn.crbz.modules.order.order.entity.dos.OrderComplaint;
import cn.crbz.modules.statistics.mapper.OrderComplaintStatisticsMapper;
import cn.crbz.modules.statistics.service.OrderComplaintStatisticsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 交易投诉业务层实现
 *
 * @author paulG
 * @since 2020/12/5
 **/
@Service
public class OrderComplaintStatisticsServiceImpl extends ServiceImpl<OrderComplaintStatisticsMapper, OrderComplaint> implements OrderComplaintStatisticsService {

    @Override
    public long waitComplainNum() {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.ne("complain_status", ComplaintStatusEnum.COMPLETE.name());
        queryWrapper.eq(StringUtils.equals(UserContext.getCurrentUser().getRole().name(), UserEnums.STORE.name()),
                "store_id", UserContext.getCurrentUser().getStoreId());
        return this.count(queryWrapper);
    }


}
