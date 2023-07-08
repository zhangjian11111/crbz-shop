package cn.crbz.controller.promotion;

import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.common.security.AuthUser;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.promotion.entity.dos.Coupon;
import cn.crbz.modules.promotion.entity.dos.MemberCoupon;
import cn.crbz.modules.promotion.entity.dto.search.CouponSearchParams;
import cn.crbz.modules.promotion.entity.dto.search.MemberCouponSearchParams;
import cn.crbz.modules.promotion.entity.vos.CouponVO;
import cn.crbz.modules.promotion.entity.vos.MemberCouponVO;
import cn.crbz.modules.promotion.service.CouponService;
import cn.crbz.modules.promotion.service.MemberCouponService;
import cn.crbz.modules.promotion.tools.PromotionTools;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端,优惠券接口
 *
 * @author paulG
 * @since 2020/10/9
 **/
@RestController
@Api(tags = "管理端,优惠券接口")
@RequestMapping("/manager/promotion/coupon")
public class CouponManagerController {
    @Autowired
    private CouponService couponService;
    @Autowired
    private MemberCouponService memberCouponService;

    @ApiOperation(value = "获取优惠券列表")
    @GetMapping
    public ResultMessage<IPage<CouponVO>> getCouponList(CouponSearchParams queryParam, PageVO page) {
        if (queryParam.getStoreId() == null) {
            queryParam.setStoreId(PromotionTools.PLATFORM_ID);
        }
        return ResultUtil.data(couponService.pageVOFindAll(queryParam, page));
    }

    @ApiOperation(value = "获取优惠券详情")
    @GetMapping("/{couponId}")
    public ResultMessage<CouponVO> getCoupon(@PathVariable String couponId) {
        CouponVO coupon = couponService.getDetail(couponId);
        return ResultUtil.data(coupon);
    }

    @ApiOperation(value = "添加优惠券")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResultMessage<CouponVO> addCoupon(@RequestBody CouponVO couponVO) {
        this.setStoreInfo(couponVO);
        couponService.savePromotions(couponVO);
        return ResultUtil.data(couponVO);
    }

    @ApiOperation(value = "修改优惠券")
    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResultMessage<Coupon> updateCoupon(@RequestBody CouponVO couponVO) {
        this.setStoreInfo(couponVO);
        Coupon coupon = couponService.getById(couponVO.getId());
        couponService.updatePromotions(couponVO);
        return ResultUtil.data(coupon);
    }

    @ApiOperation(value = "修改优惠券状态")
    @PutMapping("/status")
    public ResultMessage<Object> updateCouponStatus(String couponIds, Long startTime, Long endTime) {
        String[] split = couponIds.split(",");
        if (couponService.updateStatus(Arrays.asList(split), startTime, endTime)) {
            return ResultUtil.success(ResultCode.COUPON_EDIT_STATUS_SUCCESS);
        }
        throw new ServiceException(ResultCode.COUPON_EDIT_STATUS_ERROR);
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping(value = "/{ids}")
    public ResultMessage<Object> delAllByIds(@PathVariable List<String> ids) {
        couponService.removePromotions(ids);
        return ResultUtil.success();
    }

    @ApiOperation(value = "会员优惠券作废")
    @PutMapping(value = "/member/cancellation/{id}")
    public ResultMessage<Object> cancellation(@PathVariable String id) {
        AuthUser currentUser =  Objects.requireNonNull(UserContext.getCurrentUser());
        memberCouponService.cancellation(currentUser.getId(), id);
        return ResultUtil.success(ResultCode.COUPON_CANCELLATION_SUCCESS);
    }

    @ApiOperation(value = "根据优惠券id券分页获取会员领详情")
    @GetMapping(value = "/member/{id}")
    public ResultMessage<IPage<MemberCoupon>> getByPage(@PathVariable String id,
                                                        PageVO page) {
        QueryWrapper<MemberCoupon> queryWrapper = new QueryWrapper<>();
        IPage<MemberCoupon> data = memberCouponService.page(PageUtil.initPage(page),
                queryWrapper.eq("coupon_id", id)
        );
        return ResultUtil.data(data);

    }

    @ApiOperation(value = "获取优惠券领取详情")
    @GetMapping(value = "/received")
    public ResultMessage<IPage<MemberCouponVO>> getReceiveByPage(MemberCouponSearchParams searchParams,
                                                                 PageVO page) {
        IPage<MemberCouponVO> result = memberCouponService.getMemberCouponsPage(PageUtil.initPage(page), searchParams);
        return ResultUtil.data(result);
    }

    private void setStoreInfo(CouponVO couponVO) {
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        couponVO.setStoreId(PromotionTools.PLATFORM_ID);
        couponVO.setStoreName(PromotionTools.PLATFORM_NAME);
    }

}
