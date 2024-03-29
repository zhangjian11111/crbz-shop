package cn.crbz.modules.verification.entity.dos;

import cn.crbz.mybatis.BaseEntity;
import cn.crbz.modules.verification.entity.enums.VerificationSourceEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 验证码资源维护
 * @author Chopper
 * @since 2021/1/30 4:13 下午
 */
@Data
@TableName("crbz_verification_source")
@ApiModel(value = "验证码资源维护")
public class VerificationSource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "资源地址")
    private String resource;

    /**
     * @see VerificationSourceEnum
     */
    @ApiModelProperty(value = "验证码资源类型 SLIDER/SOURCE")
    private String type;
}
