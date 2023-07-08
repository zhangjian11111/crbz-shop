package cn.crbz.modules.member.serviceimpl;

import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.member.entity.dos.GoodsCollection;
import cn.crbz.modules.member.entity.vo.GoodsCollectionVO;
import cn.crbz.modules.member.mapper.GoodsCollectionMapper;
import cn.crbz.modules.member.service.GoodsCollectionService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 会员收藏业务层实现
 *
 * @author Chopper
 * @since 2020/11/18 2:25 下午
 */
@Service
public class GoodsCollectionServiceImpl extends ServiceImpl<GoodsCollectionMapper, GoodsCollection> implements GoodsCollectionService {


    @Override
    public IPage<GoodsCollectionVO> goodsCollection(PageVO pageVo) {
        QueryWrapper<GoodsCollectionVO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("gc.member_id", UserContext.getCurrentUser().getId());
        queryWrapper.groupBy("gc.id");
        queryWrapper.orderByDesc("gc.create_time");
        return this.baseMapper.goodsCollectionVOList(PageUtil.initPage(pageVo), queryWrapper);
    }

    @Override
    public boolean isCollection(String skuId) {
        QueryWrapper<GoodsCollection> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", UserContext.getCurrentUser().getId());
        queryWrapper.eq(skuId != null, "sku_id", skuId);
        return Optional.ofNullable(this.getOne(queryWrapper)).isPresent();
    }

    @Override
    public GoodsCollection addGoodsCollection(String skuId) {
        GoodsCollection goodsCollection = this.getOne(new LambdaUpdateWrapper<GoodsCollection>()
                .eq(GoodsCollection::getMemberId, UserContext.getCurrentUser().getId())
                .eq(GoodsCollection::getSkuId, skuId));
        if (goodsCollection == null) {
            goodsCollection = new GoodsCollection(UserContext.getCurrentUser().getId(), skuId);

            this.save(goodsCollection);
            return goodsCollection;
        }
        throw new ServiceException(ResultCode.USER_COLLECTION_EXIST);
    }

    @Override
    public boolean deleteGoodsCollection(String skuId) {
        QueryWrapper<GoodsCollection> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", UserContext.getCurrentUser().getId());
        queryWrapper.eq(skuId != null, "sku_id", skuId);
        return this.remove(queryWrapper);
    }

    @Override
    public boolean deleteGoodsCollection(List<String> goodsIds) {
        QueryWrapper<GoodsCollection> queryWrapper = new QueryWrapper();
        queryWrapper.in("sku_id", goodsIds);
        return this.remove(queryWrapper);
    }

    @Override
    public boolean deleteSkuCollection(List<String> skuIds) {
        QueryWrapper<GoodsCollection> queryWrapper = new QueryWrapper();
        queryWrapper.in("sku_id", skuIds);
        return this.remove(queryWrapper);
    }
}
