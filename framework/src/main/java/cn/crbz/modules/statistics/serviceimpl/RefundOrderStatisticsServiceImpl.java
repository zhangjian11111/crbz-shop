package cn.crbz.modules.statistics.serviceimpl;

import cn.crbz.common.utils.DateUtil;
import cn.crbz.common.utils.StringUtils;
import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.order.order.entity.dos.StoreFlow;
import cn.crbz.modules.order.order.entity.enums.FlowTypeEnum;
import cn.crbz.modules.statistics.entity.dto.StatisticsQueryParam;
import cn.crbz.modules.statistics.entity.enums.TimeTypeEnum;
import cn.crbz.modules.statistics.entity.vo.RefundOrderStatisticsDataVO;
import cn.crbz.modules.statistics.mapper.RefundOrderStatisticsMapper;
import cn.crbz.modules.statistics.service.RefundOrderStatisticsService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 退款订单统计业务层实现
 *
 * @author Bulbasaur
 * @since 2020/12/10 11:30
 */
@Service
public class RefundOrderStatisticsServiceImpl extends ServiceImpl<RefundOrderStatisticsMapper, StoreFlow> implements RefundOrderStatisticsService {

    @Override
    public IPage<RefundOrderStatisticsDataVO> getRefundOrderStatisticsData(PageVO pageVO, StatisticsQueryParam statisticsQueryParam) {
        QueryWrapper queryWrapper = getQueryWrapper(statisticsQueryParam);
        return this.baseMapper.getRefundStatisticsData(PageUtil.initPage(pageVO), queryWrapper);
    }

    @Override
    public Double getRefundOrderStatisticsPrice(StatisticsQueryParam statisticsQueryParam) {

        QueryWrapper queryWrapper = getQueryWrapper(statisticsQueryParam);
        queryWrapper.select("SUM(final_price) AS price");
        return Double.parseDouble(this.getMap(queryWrapper).get("price").toString());
    }


    private QueryWrapper getQueryWrapper(StatisticsQueryParam statisticsQueryParam) {

        QueryWrapper queryWrapper = Wrappers.query();

        //判断搜索类型是：年、月
        if (statisticsQueryParam.getTimeType().equals(TimeTypeEnum.MONTH.name())) {
            queryWrapper.between("create_time", DateUtil.getBeginTime(statisticsQueryParam.getYear(), statisticsQueryParam.getMonth()), DateUtil.getEndTime(statisticsQueryParam.getYear(), statisticsQueryParam.getMonth()));
        } else {
            queryWrapper.between("create_time", DateUtil.getBeginTime(statisticsQueryParam.getYear(), 1), DateUtil.getEndTime(statisticsQueryParam.getYear(), 12));
        }

        //设置店铺ID
        queryWrapper.eq(!StringUtils.isEmpty(statisticsQueryParam.getStoreId()), "store_id", statisticsQueryParam.getStoreId());

        //设置为退款查询
        queryWrapper.eq("flow_type", FlowTypeEnum.REFUND);
        return queryWrapper;
    }
}
