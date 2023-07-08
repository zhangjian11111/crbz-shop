package cn.crbz.event.impl;


import cn.crbz.cache.Cache;
import cn.crbz.cache.CachePrefix;
import cn.crbz.event.GoodsCommentCompleteEvent;
import cn.crbz.event.StoreSettingChangeEvent;
import cn.crbz.modules.goods.entity.dos.GoodsSku;
import cn.crbz.modules.goods.entity.dto.GoodsSearchParams;
import cn.crbz.modules.goods.service.GoodsSkuService;
import cn.crbz.modules.member.entity.dos.MemberEvaluation;
import cn.crbz.modules.store.entity.dos.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品SKU变化
 *
 * @author Chopper
 * @since 2020-07-03 11:20
 */
@Service
public class GoodsSkuExecute implements GoodsCommentCompleteEvent, StoreSettingChangeEvent {

    /**
     * 商品
     */
    @Autowired
    private GoodsSkuService goodsSkuService;

    @Autowired
    private Cache cache;

    @Override
    public void goodsComment(MemberEvaluation memberEvaluation) {
        goodsSkuService.updateGoodsSkuCommentNum(memberEvaluation.getSkuId());
    }

    @Override
    public void storeSettingChange(Store store) {
        //修改数据后，清除商品索引
        GoodsSearchParams goodsSearchParams = new GoodsSearchParams();
        goodsSearchParams.setStoreId(store.getId());
        List<String> goodsSkuKeys = new ArrayList<>();
        for (GoodsSku goodsSku : goodsSkuService.getGoodsSkuByList(goodsSearchParams)) {
            goodsSkuKeys.add(CachePrefix.GOODS_SKU.getPrefix()+goodsSku.getId());
        }
        cache.multiDel(goodsSkuKeys);
    }
}
