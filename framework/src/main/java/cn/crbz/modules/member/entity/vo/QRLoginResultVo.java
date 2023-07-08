package cn.crbz.modules.member.entity.vo;

import cn.crbz.common.security.token.Token;
import lombok.Data;

@Data
public class QRLoginResultVo {

    private Token token;

    private int status;
}
