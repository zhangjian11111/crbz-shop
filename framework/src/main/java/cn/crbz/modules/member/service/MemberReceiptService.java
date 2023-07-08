package cn.crbz.modules.member.service;


import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.member.entity.dos.MemberReceipt;
import cn.crbz.modules.member.entity.vo.MemberReceiptAddVO;
import cn.crbz.modules.member.entity.vo.MemberReceiptVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 会员发票业务层
 *
 * @author Chopper
 * @since 2021-03-29 14:10:16
 */
public interface MemberReceiptService extends IService<MemberReceipt> {

    /**
     * 查询会员发票列表
     *
     * @param memberReceiptVO 会员发票信息
     * @param pageVO          分页信息
     * @return 会员发票分页
     */
    IPage<MemberReceipt> getPage(MemberReceiptVO memberReceiptVO, PageVO pageVO);

    /**
     * 添加会员发票信息
     *
     * @param memberReceiptAddVO 会员发票信息
     * @param memberId           会员ID
     * @return 操作状态
     */
    Boolean addMemberReceipt(MemberReceiptAddVO memberReceiptAddVO, String memberId);

    /**
     * 修改会员发票信息
     *
     * @param memberReceiptAddVO 会员发票信息
     * @param memberId           会员ID
     * @return 操作状态
     */
    Boolean editMemberReceipt(MemberReceiptAddVO memberReceiptAddVO, String memberId);

    /**
     * 删除会员发票信息
     *
     * @param id 发票ID
     * @return 操作状态
     */
    Boolean deleteMemberReceipt(String id);

}
