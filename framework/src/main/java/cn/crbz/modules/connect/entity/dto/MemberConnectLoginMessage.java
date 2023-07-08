package cn.crbz.modules.connect.entity.dto;

import cn.crbz.modules.member.entity.dos.Member;
import lombok.Data;

/**
 * 会员联合登录消息
 */
@Data
public class MemberConnectLoginMessage {

    private Member member;
    private ConnectAuthUser connectAuthUser;
}
