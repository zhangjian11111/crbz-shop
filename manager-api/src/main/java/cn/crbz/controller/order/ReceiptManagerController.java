package cn.crbz.controller.order;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.order.entity.dto.OrderReceiptDTO;
import cn.crbz.modules.order.order.entity.dto.ReceiptSearchParams;
import cn.crbz.modules.order.order.service.ReceiptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,发票记录接口
 *
 * @author paulG
 * @since 2020/11/17 4:34 下午
 **/
@RestController
@Api(tags = "管理端,发票记录接口")
@RequestMapping("/manager/trade/receipt")
public class ReceiptManagerController {

    @Autowired
    private ReceiptService receiptService;


    @ApiOperation(value = "获取发票分页信息")
    @GetMapping
    public ResultMessage<IPage<OrderReceiptDTO>> getPage(ReceiptSearchParams searchParams, PageVO pageVO) {
        return ResultUtil.data(this.receiptService.getReceiptData(searchParams, pageVO));
    }

}
