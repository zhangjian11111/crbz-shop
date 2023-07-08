package cn.crbz.modules.page.entity.dos;

import cn.crbz.mybatis.BaseEntity;
import cn.crbz.common.enums.ClientTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 专题活动
 *
 * @author Bulbasaur
 * @since 2020/12/10 17:42
 */
@Data
@TableName("crbz_special")
@ApiModel(value = "专题活动")
public class Special extends BaseEntity {

    @ApiModelProperty(value = "专题活动名称")
    private String specialName;

    /**
     * @see ClientTypeEnum
     */
    @ApiModelProperty(value = "楼层对应连接端类型", allowableValues = "PC,H5,WECHAT_MP,APP")
    private String clientType;

    @ApiModelProperty(value = "页面ID")
    private String pageDataId;
}
