package cn.crbz.timetask.handler.impl.hotwords;

import cn.hutool.json.JSONUtil;
import cn.crbz.cache.Cache;
import cn.crbz.cache.CachePrefix;
import cn.crbz.modules.search.entity.dos.HotWordsHistory;
import cn.crbz.modules.search.service.HotWordsHistoryService;
import cn.crbz.modules.system.entity.dos.Setting;
import cn.crbz.modules.system.entity.dto.HotWordsSetting;
import cn.crbz.modules.system.entity.dto.HotWordsSettingItem;
import cn.crbz.modules.system.entity.enums.SettingEnum;
import cn.crbz.modules.system.service.SettingService;
import cn.crbz.timetask.handler.EveryDayExecute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author paulG
 * @since 2021/3/11
 **/
@Slf4j
@Component
public class HotWordsEveryDayTaskExecute implements EveryDayExecute {

    @Autowired
    private Cache cache;

    @Autowired
    private HotWordsHistoryService hotWordsHistoryService;
    @Autowired
    private SettingService settingService;

    /**
     * 执行每日任务
     */
    @Override
    public void execute() {
        //获取大于0分的热词
        Set<DefaultTypedTuple> tuples = cache.zRangeByScore(CachePrefix.HOT_WORD.getPrefix(), 1, Integer.MAX_VALUE);
        //如果任务不为空
        if (!CollectionUtils.isEmpty(tuples)) {

            //因为是第二天统计第一天的数据，所以这里获取昨天凌晨的时间
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);

            //批量保存热词
            List<HotWordsHistory> hotWordsHistories = new ArrayList<>();
            for (DefaultTypedTuple tuple : tuples) {
                String keywords = (String) tuple.getValue();
                Double score = tuple.getScore();
                hotWordsHistories.add(new HotWordsHistory(keywords, score.intValue(), calendar.getTime()));
            }

            hotWordsHistoryService.saveBatch(hotWordsHistories);
        }
        //移除昨日的热搜词
        cache.remove(CachePrefix.HOT_WORD.getPrefix());

        //设置今日默认热词
        Setting setting = settingService.get(SettingEnum.HOT_WORDS.name());
        if (setting == null) {
            return;
        }
        HotWordsSetting hotWordsSetting = JSONUtil.toBean(setting.getSettingValue(), HotWordsSetting.class);
        List<HotWordsSettingItem> hotWordsSettingItems = hotWordsSetting.getHotWordsSettingItems();
        if (hotWordsSettingItems != null && !hotWordsSettingItems.isEmpty()) {
            for (HotWordsSettingItem hotWordsSettingItem : hotWordsSettingItems) {
                cache.zAdd(CachePrefix.HOT_WORD.getPrefix(), hotWordsSettingItem.getScore(), hotWordsSettingItem.getKeywords());
            }
        }
    }

}
