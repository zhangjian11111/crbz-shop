package cn.crbz.controller.order;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.order.entity.dos.Receipt;
import cn.crbz.modules.order.order.entity.dto.OrderReceiptDTO;
import cn.crbz.modules.order.order.entity.dto.ReceiptSearchParams;
import cn.crbz.modules.order.order.service.ReceiptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 买家端,发票接口
 *
 * @author paulG
 * @since 2021/1/12
 **/
@RestController
@Api(tags = "买家端,发票接口")
@RequestMapping("/buyer/trade/receipt")
public class ReceiptBuyerController {

    @Autowired
    private ReceiptService receiptService;

    @ApiOperation(value = "获取发票详情")
    @GetMapping("/{id}")
    public ResultMessage<Receipt> getDetail(@PathVariable String id) {
        return ResultUtil.data(this.receiptService.getDetail(id));
    }

    @ApiOperation(value = "获取发票分页信息")
    @GetMapping
    public ResultMessage<IPage<OrderReceiptDTO>> getPage(ReceiptSearchParams searchParams, PageVO pageVO) {
        return ResultUtil.data(this.receiptService.getReceiptData(searchParams, pageVO));
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "保存发票信息")
    @PostMapping
    public ResultMessage<Receipt> save(@Valid Receipt receipt) {
        return ResultUtil.data(receiptService.saveReceipt(receipt));
    }

}
