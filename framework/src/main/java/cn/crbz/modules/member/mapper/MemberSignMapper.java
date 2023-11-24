package cn.crbz.modules.member.mapper;


import cn.crbz.modules.member.entity.dos.MemberSign;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会员签到数据处理层
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
public interface MemberSignMapper extends BaseMapper<MemberSign> {

    /**
     * 获取会员之前签到信息
     *
     * @param memberId 会员ID
     * @return 会员签到列表
     */
    @Select("SELECT * FROM crbz_member_sign WHERE TO_DAYS( NOW( ) ) - TO_DAYS( create_time) = 1 and member_id = #{memberId}")
    List<MemberSign> getBeforeMemberSign(String memberId);

    /**
     * 获取会员签到
     *
     * @param queryWrapper 查询条件
     * @return 会员签到列表
     */
    @Select("select * from crbz_member_sign ${ew.customSqlSegment}")
    List<MemberSign> getTodayMemberSign(@Param(Constants.WRAPPER) Wrapper<MemberSign> queryWrapper);

    /**
     * 获取当月的会员签到记录
     *
     * @param memberId 会员ID
     * @param time     时间
     * @return 会员签到列表
     */
    @Select("SELECT * FROM crbz_member_sign WHERE DATE_FORMAT(create_time,'%Y%m') = #{arg1} and member_id = #{arg0}")
    List<MemberSign> getMonthMemberSign(String memberId, String time);

}
