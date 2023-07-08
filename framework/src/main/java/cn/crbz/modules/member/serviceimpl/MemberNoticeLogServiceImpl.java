package cn.crbz.modules.member.serviceimpl;

import cn.crbz.modules.member.entity.dos.MemberNoticeLog;
import cn.crbz.modules.member.mapper.MemberNoticeLogMapper;
import cn.crbz.modules.member.service.MemberNoticeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 会员消息业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 3:44 下午
 */
@Service
public class MemberNoticeLogServiceImpl extends ServiceImpl<MemberNoticeLogMapper, MemberNoticeLog> implements MemberNoticeLogService {
}
