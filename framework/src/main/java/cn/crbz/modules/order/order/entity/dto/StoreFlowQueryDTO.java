package cn.crbz.modules.order.order.entity.dto;

import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.store.entity.dos.Bill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 店铺流水查询DTO
 *
 * @author Chopper
 * @version v1.0
 * 2021-12-08 10:53
 */
@Data
@Builder
public class StoreFlowQueryDTO {

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "售后编号")
    private String refundSn;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "订单货物编号")
    private String orderItemSn;

    @ApiModelProperty(value = "过滤只看分销订单")
    private Boolean justDistribution;

    @ApiModelProperty("结算单")
    private Bill bill;

    @ApiModelProperty(value = "分页")
    private PageVO pageVO;

}
