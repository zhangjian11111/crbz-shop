package cn.crbz.modules.member.service;

import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.member.entity.dos.GoodsCollection;
import cn.crbz.modules.member.entity.vo.GoodsCollectionVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品收藏业务层
 *
 * @author Chopper
 * @since 2020/11/18 2:25 下午
 */
public interface GoodsCollectionService extends IService<GoodsCollection> {

    /**
     * 获取商品搜索分页
     *
     * @param pageVo 查询参数
     * @return 商品搜索分页
     */
    IPage<GoodsCollectionVO> goodsCollection(PageVO pageVo);

    /**
     * 是否收藏商品
     *
     * @param skuId 规格ID
     * @return 是否收藏
     */
    boolean isCollection(String skuId);

    /**
     * 添加商品收藏
     *
     * @param skuId 规格ID
     * @return 操作状态
     */
    GoodsCollection addGoodsCollection(String skuId);

    /**
     * 商品收藏
     *
     * @param skuId 规格ID
     * @return 操作状态
     */
    boolean deleteGoodsCollection(String skuId);
    /**
     * 删除商品收藏
     *
     * @param goodsIds 规格ID
     * @return 操作状态
     */
    boolean deleteGoodsCollection(List<String> goodsIds);
    /**
     * 删除商品SKU收藏
     *
     * @param skuIds 规格ID
     * @return 操作状态
     */
    boolean deleteSkuCollection(List<String> skuIds);
}
