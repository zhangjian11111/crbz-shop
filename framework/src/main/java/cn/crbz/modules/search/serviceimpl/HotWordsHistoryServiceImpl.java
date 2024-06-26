package cn.crbz.modules.search.serviceimpl;

import cn.crbz.common.utils.DateUtil;
import cn.crbz.modules.search.entity.dos.HotWordsHistory;
import cn.crbz.modules.search.entity.dto.HotWordsSearchParams;
import cn.crbz.modules.search.mapper.HotWordsHistoryMapper;
import cn.crbz.modules.search.service.HotWordsHistoryService;
import cn.crbz.modules.search.service.HotWordsService;
import cn.crbz.modules.statistics.entity.enums.SearchTypeEnum;
import cn.crbz.modules.statistics.util.StatisticsDateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 历史热词
 *
 * @author paulG
 * @since 2020/10/15
 **/
@Service
public class HotWordsHistoryServiceImpl extends ServiceImpl<HotWordsHistoryMapper, HotWordsHistory> implements HotWordsHistoryService {

    @Autowired
    private HotWordsService hotWordsService;

    @Override
    public List<HotWordsHistory> statistics(HotWordsSearchParams hotWordsSearchParams) {
        if (hotWordsSearchParams.getSearchType().equals(SearchTypeEnum.TODAY.name())) {
            return hotWordsService.getHotWordsVO(hotWordsSearchParams.getTop());
        }
        QueryWrapper queryWrapper = hotWordsSearchParams.queryWrapper();

        queryWrapper.groupBy("keywords");
        queryWrapper.orderByDesc("score");
        queryWrapper.last("limit " + hotWordsSearchParams.getTop());
        List<HotWordsHistory> list = baseMapper.statistics(queryWrapper);
        return list;
    }

    @Override
    public List<HotWordsHistory> queryByDay(Date queryTime) {
        QueryWrapper queryWrapper = new QueryWrapper();

        Date[] dates = StatisticsDateUtil.getDateArray(queryTime);
        queryWrapper.between("create_time", dates[0], dates[1]);
        return list(queryWrapper);
    }

}
