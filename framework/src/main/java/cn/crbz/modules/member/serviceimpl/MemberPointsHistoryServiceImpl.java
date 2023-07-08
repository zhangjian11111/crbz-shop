package cn.crbz.modules.member.serviceimpl;


import cn.hutool.core.text.CharSequenceUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.member.entity.dos.Member;
import cn.crbz.modules.member.entity.dos.MemberPointsHistory;
import cn.crbz.modules.member.entity.vo.MemberPointsHistoryVO;
import cn.crbz.modules.member.mapper.MemberPointsHistoryMapper;
import cn.crbz.modules.member.service.MemberPointsHistoryService;
import cn.crbz.modules.member.service.MemberService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员积分历史业务层实现
 *
 * @author Bulbasaur
 * @since 2020-02-25 14:10:16
 */
@Service
public class MemberPointsHistoryServiceImpl extends ServiceImpl<MemberPointsHistoryMapper, MemberPointsHistory> implements MemberPointsHistoryService {


    @Autowired
    private MemberService memberService;

    @Override
    public MemberPointsHistoryVO getMemberPointsHistoryVO(String memberId) {
        //获取会员积分历史
        Member member = memberService.getById(memberId);
        MemberPointsHistoryVO memberPointsHistoryVO = new MemberPointsHistoryVO();
        if (member != null) {
            memberPointsHistoryVO.setPoint(member.getPoint());
            memberPointsHistoryVO.setTotalPoint(member.getTotalPoint());
            return memberPointsHistoryVO;
        }
        return new MemberPointsHistoryVO();
    }

    @Override
    public IPage<MemberPointsHistory> MemberPointsHistoryList(PageVO page, String memberId, String memberName) {
        LambdaQueryWrapper<MemberPointsHistory> lambdaQueryWrapper = new LambdaQueryWrapper<MemberPointsHistory>()
                .eq(CharSequenceUtil.isNotEmpty(memberId), MemberPointsHistory::getMemberId, memberId)
                .like(CharSequenceUtil.isNotEmpty(memberName), MemberPointsHistory::getMemberName, memberName);
        //如果排序为空，则默认创建时间倒序
        if (CharSequenceUtil.isEmpty(page.getSort())) {
            page.setSort("createTime");
            page.setOrder("desc");
        }
        return this.page(PageUtil.initPage(page), lambdaQueryWrapper);
    }

}
