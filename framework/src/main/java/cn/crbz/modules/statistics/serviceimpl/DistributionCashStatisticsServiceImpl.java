package cn.crbz.modules.statistics.serviceimpl;

import cn.crbz.modules.distribution.entity.dos.DistributionCash;
import cn.crbz.modules.statistics.mapper.DistributionCashStatisticsMapper;
import cn.crbz.modules.statistics.service.DistributionCashStatisticsService;
import cn.crbz.modules.wallet.entity.enums.WithdrawStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 分销佣金统计层实现
 *
 * @author pikachu
 * @since 2020-03-126 18:04:56
 */
@Service
public class DistributionCashStatisticsServiceImpl extends ServiceImpl<DistributionCashStatisticsMapper, DistributionCash>
        implements DistributionCashStatisticsService {


    @Override
    public long newDistributionCash() {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("distribution_cash_status", WithdrawStatusEnum.APPLY.name());
        return this.count(queryWrapper);
    }
}
