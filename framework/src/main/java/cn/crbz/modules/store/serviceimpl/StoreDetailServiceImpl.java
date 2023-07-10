package cn.crbz.modules.store.serviceimpl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import cn.crbz.cache.Cache;
import cn.crbz.cache.CachePrefix;
import cn.crbz.common.properties.RocketmqCustomProperties;
import cn.crbz.common.security.AuthUser;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.utils.BeanUtil;
import cn.crbz.modules.goods.entity.dos.Category;
import cn.crbz.modules.goods.service.CategoryService;
import cn.crbz.modules.goods.service.GoodsService;
import cn.crbz.modules.search.utils.EsIndexUtil;
import cn.crbz.modules.store.entity.dos.Store;
import cn.crbz.modules.store.entity.dos.StoreDetail;
import cn.crbz.modules.store.entity.dto.StoreAfterSaleAddressDTO;
import cn.crbz.modules.store.entity.dto.StoreDeliverGoodsAddressDTO;
import cn.crbz.modules.store.entity.dto.StoreSettingDTO;
import cn.crbz.modules.store.entity.dto.StoreSettlementDay;
import cn.crbz.modules.store.entity.vos.StoreBasicInfoVO;
import cn.crbz.modules.store.entity.vos.StoreDetailVO;
import cn.crbz.modules.store.entity.vos.StoreManagementCategoryVO;
import cn.crbz.modules.store.entity.vos.StoreOtherVO;
import cn.crbz.modules.store.mapper.StoreDetailMapper;
import cn.crbz.modules.store.service.StoreDetailService;
import cn.crbz.modules.store.service.StoreService;
import cn.crbz.rocketmq.RocketmqSendCallbackBuilder;
import cn.crbz.rocketmq.tags.GoodsTagsEnum;
import cn.crbz.rocketmq.tags.StoreTagsEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 店铺详细业务层实现
 *
 * @author pikachu
 * @since 2020-03-07 16:18:56
 */
@Service
public class StoreDetailServiceImpl extends ServiceImpl<StoreDetailMapper, StoreDetail> implements StoreDetailService {

    /**
     * 店铺
     */
    @Autowired
    private StoreService storeService;
    /**
     * 分类
     */
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private Cache cache;

    @Override
    public StoreDetailVO getStoreDetailVO(String storeId) {
        StoreDetailVO storeDetailVO = (StoreDetailVO) cache.get(CachePrefix.STORE.getPrefix() + storeId);
        if (storeDetailVO == null) {
            storeDetailVO = this.baseMapper.getStoreDetail(storeId);
            cache.put(CachePrefix.STORE.getPrefix() + storeId, storeDetailVO, 7200L);
        }
        return storeDetailVO;
    }

    @Override
    public StoreDetailVO getStoreDetailVOByMemberId(String memberId) {
        return this.baseMapper.getStoreDetailByMemberId(memberId);
    }

    @Override
    public StoreDetail getStoreDetail(String storeId) {
        LambdaQueryWrapper<StoreDetail> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(StoreDetail::getStoreId, storeId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public Boolean editStoreSetting(StoreSettingDTO storeSettingDTO) {
        AuthUser tokenUser = Objects.requireNonNull(UserContext.getCurrentUser());
        //修改店铺
        Store store = storeService.getById(tokenUser.getStoreId());
        BeanUtil.copyProperties(storeSettingDTO, store);
        boolean result = storeService.updateById(store);
        if (result) {
            this.updateStoreGoodsInfo(store);
            this.removeCache(store.getId());
        }
        String destination = rocketmqCustomProperties.getStoreTopic() + ":" + StoreTagsEnum.EDIT_STORE_SETTING.name();
        //发送订单变更mq消息
        rocketMQTemplate.asyncSend(destination, store, RocketmqSendCallbackBuilder.commonCallback());
        return result;
    }

    @Override
    public void updateStoreGoodsInfo(Store store) {

        goodsService.updateStoreDetail(store);

        Map<String, Object> updateIndexFieldsMap = EsIndexUtil.getUpdateIndexFieldsMap(
                MapUtil.builder(new HashMap<String, Object>()).put("storeId", store.getId()).build(),
                MapUtil.builder(new HashMap<String, Object>()).put("storeName", store.getStoreName()).put("selfOperated", store.getSelfOperated()).build());
        String destination = rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.UPDATE_GOODS_INDEX_FIELD.name();
        //发送mq消息
        rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(updateIndexFieldsMap), RocketmqSendCallbackBuilder.commonCallback());
    }

    @Override
    public Boolean editMerchantEuid(String merchantEuid) {
        AuthUser tokenUser = Objects.requireNonNull(UserContext.getCurrentUser());
        Store store = storeService.getById(tokenUser.getStoreId());
        store.setMerchantEuid(merchantEuid);
        this.removeCache(store.getId());
        return storeService.updateById(store);
    }

    /**
     * 获取待结算店铺列表
     *
     * @param day 结算日
     * @return 待结算店铺列表
     */
    @Override
    public List<StoreSettlementDay> getSettlementStore(int day) {
        return this.baseMapper.getSettlementStore(day);
    }

    @Override
    public StoreDeliverGoodsAddressDTO getStoreDeliverGoodsAddressDto() {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser().getStoreId());
        return this.baseMapper.getStoreDeliverGoodsAddressDto(storeId);
    }

    @Override
    public StoreDeliverGoodsAddressDTO getStoreDeliverGoodsAddressDto(String id) {
        StoreDeliverGoodsAddressDTO storeDeliverGoodsAddressDto = this.baseMapper.getStoreDeliverGoodsAddressDto(id);
        if (storeDeliverGoodsAddressDto == null) {
            storeDeliverGoodsAddressDto = new StoreDeliverGoodsAddressDTO();
        }
        return storeDeliverGoodsAddressDto;
    }

    @Override
    public boolean editStoreDeliverGoodsAddressDTO(StoreDeliverGoodsAddressDTO storeDeliverGoodsAddressDto) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser().getStoreId());
        LambdaUpdateWrapper<StoreDetail> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsignorName, storeDeliverGoodsAddressDto.getSalesConsignorName());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsignorMobile, storeDeliverGoodsAddressDto.getSalesConsignorMobile());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsignorAddressId, storeDeliverGoodsAddressDto.getSalesConsignorAddressId());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsignorAddressPath, storeDeliverGoodsAddressDto.getSalesConsignorAddressPath());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsignorDetail, storeDeliverGoodsAddressDto.getSalesConsignorDetail());
        lambdaUpdateWrapper.eq(StoreDetail::getStoreId, storeId);


        this.removeCache(storeId);
        return this.update(lambdaUpdateWrapper);
    }

    /**
     * 修改店铺的结算日
     *
     * @param storeId  店铺ID
     * @param dateTime 结算日
     */
    @Override
    public void updateSettlementDay(String storeId, DateTime dateTime) {
        this.removeCache(storeId);
        this.baseMapper.updateSettlementDay(storeId, dateTime);
    }

    @Override
    public StoreBasicInfoVO getStoreBasicInfoDTO(String storeId) {
        return this.baseMapper.getStoreBasicInfoDTO(storeId);
    }

    @Override
    public StoreAfterSaleAddressDTO getStoreAfterSaleAddressDTO() {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        return this.baseMapper.getStoreAfterSaleAddressDTO(storeId);
    }


    @Override
    public StoreAfterSaleAddressDTO getStoreAfterSaleAddressDTO(String id) {
        StoreAfterSaleAddressDTO storeAfterSaleAddressDTO = this.baseMapper.getStoreAfterSaleAddressDTO(id);
        if (storeAfterSaleAddressDTO == null) {
            storeAfterSaleAddressDTO = new StoreAfterSaleAddressDTO();
        }
        return storeAfterSaleAddressDTO;
    }

    @Override
    public boolean editStoreAfterSaleAddressDTO(StoreAfterSaleAddressDTO storeAfterSaleAddressDTO) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        LambdaUpdateWrapper<StoreDetail> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeName, storeAfterSaleAddressDTO.getSalesConsigneeName());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeAddressId, storeAfterSaleAddressDTO.getSalesConsigneeAddressId());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeAddressPath, storeAfterSaleAddressDTO.getSalesConsigneeAddressPath());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeDetail, storeAfterSaleAddressDTO.getSalesConsigneeDetail());
        lambdaUpdateWrapper.set(StoreDetail::getSalesConsigneeMobile, storeAfterSaleAddressDTO.getSalesConsigneeMobile());
        lambdaUpdateWrapper.eq(StoreDetail::getStoreId, storeId);

        this.removeCache(storeId);
        return this.update(lambdaUpdateWrapper);
    }


    @Override
    public boolean updateStockWarning(Integer stockWarning) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        LambdaUpdateWrapper<StoreDetail> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(StoreDetail::getStockWarning, stockWarning);
        lambdaUpdateWrapper.eq(StoreDetail::getStoreId, storeId);
        this.removeCache(storeId);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    public List<StoreManagementCategoryVO> goodsManagementCategory(String storeId) {

        //获取顶部分类列表
        List<Category> categoryList = categoryService.firstCategory();
        //获取店铺信息
        StoreDetail storeDetail = this.getOne(new LambdaQueryWrapper<StoreDetail>().eq(StoreDetail::getStoreId, storeId));
        //获取店铺分类
        String[] storeCategoryList = storeDetail.getGoodsManagementCategory().split(",");
        List<StoreManagementCategoryVO> list = new ArrayList<>();
        for (Category category : categoryList) {
            StoreManagementCategoryVO storeManagementCategoryVO = new StoreManagementCategoryVO(category);
            for (String storeCategory : storeCategoryList) {
                if (storeCategory.equals(category.getId())) {
                    storeManagementCategoryVO.setSelected(true);
                }
            }
            list.add(storeManagementCategoryVO);
        }
        return list;
    }

    @Override
    public StoreOtherVO getStoreOtherVO(String storeId) {
        return this.baseMapper.getLicencePhoto(storeId);
    }


    /**
     * 删除缓存
     *
     * @param storeId 店铺id
     */
    private void removeCache(String storeId) {
        cache.remove(CachePrefix.STORE.getPrefix() + storeId);
    }

}