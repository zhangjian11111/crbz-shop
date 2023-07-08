package cn.crbz.modules.statistics.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.security.enums.UserEnums;
import cn.crbz.modules.statistics.mapper.BillStatisticsMapper;
import cn.crbz.modules.statistics.service.BillStatisticsService;
import cn.crbz.modules.store.entity.dos.Bill;
import cn.crbz.modules.store.entity.enums.BillStatusEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 结算单统计
 *
 * @author Chopper
 * @since 2020/11/17 4:28 下午
 */
@Service
public class BillStatisticsServiceImpl extends ServiceImpl<BillStatisticsMapper, Bill> implements BillStatisticsService {


    @Override
    public long billNum(BillStatusEnum billStatusEnum) {
        LambdaUpdateWrapper<Bill> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.eq(Bill::getBillStatus, billStatusEnum.name());
        lambdaUpdateWrapper.eq(CharSequenceUtil.equals(Objects.requireNonNull(UserContext.getCurrentUser()).getRole().name(), UserEnums.STORE.name()),
                Bill::getStoreId, UserContext.getCurrentUser().getStoreId());
        return this.count(lambdaUpdateWrapper);
    }


}
