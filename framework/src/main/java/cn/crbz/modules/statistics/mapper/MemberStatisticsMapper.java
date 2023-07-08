package cn.crbz.modules.statistics.mapper;

import cn.crbz.modules.member.entity.vo.MemberDistributionVO;
import cn.crbz.modules.statistics.entity.dos.MemberStatisticsData;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会员统计数据处理层
 *
 * @author Bulbasaur
 * @since 2020/11/17 7:34 下午
 */
public interface MemberStatisticsMapper extends BaseMapper<MemberStatisticsData> {

    /**
     * 获取会员统计数量
     *
     * @param queryWrapper 查询条件
     * @return 会员统计数量
     */
    @Select("SELECT  COUNT(0)  FROM crbz_member  ${ew.customSqlSegment}")
    long customSqlQuery(@Param(Constants.WRAPPER) Wrapper queryWrapper);


    /**
     * 获取会员分布列表
     * @return 会员分布列表
     */
    @Select("select client_enum,count(0) as num from crbz_member group by client_enum")
    List<MemberDistributionVO> distribution();
}
