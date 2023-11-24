package cn.crbz.modules.member.mapper;


import cn.crbz.modules.member.entity.dos.Clerk;
import cn.crbz.modules.member.entity.vo.ClerkVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 店员数据处理层
 *
 * @author wget
 * @title: ClerkMapper
 * @projectName crbzshop
 * @date 2021/12/28 7:39 下午
 */
public interface ClerkMapper extends BaseMapper<Clerk> {

    /**
     * 查询店员分页数据
     * @param page 分页信息
     * @param ew 店铺ID
     * @return
     */
    @Select("select crbz_clerk.*,m.id,m.mobile as mobile from crbz_clerk inner join crbz_member as m on crbz_clerk.member_id = m.id ${ew.customSqlSegment}")
    IPage<ClerkVO> selectClerkPage(Page page, @Param(Constants.WRAPPER) QueryWrapper ew);


}
