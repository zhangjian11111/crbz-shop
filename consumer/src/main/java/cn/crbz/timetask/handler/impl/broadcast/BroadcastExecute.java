package cn.crbz.timetask.handler.impl.broadcast;

import cn.crbz.modules.goods.service.CommodityService;
import cn.crbz.timetask.handler.EveryHourExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 小程序直播状态获取
 *
 * @author Bulbasaur
 * @since 2021/5/20 2:52 下午
 */
@Component
public class BroadcastExecute implements EveryHourExecute {

    @Autowired
    private CommodityService commodityService;

    @Override
    public void execute() {
        //同步直播商品状态
        commodityService.getGoodsWareHouse();
    }
}
