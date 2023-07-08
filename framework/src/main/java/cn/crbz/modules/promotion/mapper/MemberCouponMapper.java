package cn.crbz.modules.promotion.mapper;

import cn.crbz.modules.promotion.entity.dos.MemberCoupon;
import cn.crbz.modules.promotion.entity.vos.MemberCouponVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会员优惠券数据处理层
 *
 * @author Chopper
 * @since 2020/8/21
 */
public interface MemberCouponMapper extends BaseMapper<MemberCoupon> {

    @Select("SELECT mc.*,c.coupon_name FROM crbz_member_coupon mc LEFT JOIN crbz_coupon c ON mc.coupon_id = c.id ${ew.customSqlSegment}")
    Page<MemberCouponVO> getMemberCoupons(Page<MemberCoupon> page, @Param(Constants.WRAPPER) Wrapper<MemberCouponVO> queryWrapper);

}
