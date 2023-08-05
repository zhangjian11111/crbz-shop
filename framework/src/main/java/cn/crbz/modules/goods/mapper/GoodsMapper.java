package cn.crbz.modules.goods.mapper;

import cn.crbz.modules.goods.entity.dos.Goods;
import cn.crbz.modules.goods.entity.vos.GoodsVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 规格项数据处理层
 *
 * @author pikachu
 * @since 2020-02-18 15:18:56
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 根据店铺ID获取商品ID列表
     *
     * @param storeId 店铺ID
     * @return 商品ID列表
     */
    @Select("SELECT id FROM crbz_goods WHERE store_id = #{storeId}")
    List<String> getGoodsIdByStoreId(String storeId);

    /**
     * 添加商品评价数量
     *
     * @param commentNum 评价数量
     * @param goodsId    商品ID
     */
    @Update("UPDATE crbz_goods SET comment_num = comment_num + #{commentNum} WHERE id = #{goodsId}")
    void addGoodsCommentNum(@Param("commentNum") Integer commentNum,@Param("goodsId") String goodsId);

    /**
     * 查询商品VO分页
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return 商品VO分页
     */
    @Select("select g.* from crbz_goods as g ")
    IPage<GoodsVO> queryByParams(IPage<GoodsVO> page, @Param(Constants.WRAPPER) Wrapper<GoodsVO> queryWrapper);
}
