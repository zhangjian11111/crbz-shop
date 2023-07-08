package cn.crbz.controller.order;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.common.vo.SearchVO;
import cn.crbz.modules.payment.entity.RefundLog;
import cn.crbz.modules.payment.service.RefundLogService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,退款日志接口
 *
 * @author Chopper
 * @since 2020/11/16 10:07 下午
 */
@RestController
@Api(tags = "管理端,退款日志接口")
@RequestMapping("/manager/order/refundLog")
public class RefundLogManagerController {
    @Autowired
    private RefundLogService refundLogService;

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查看退款日志详情")
    public ResultMessage<RefundLog> get(@PathVariable String id) {
        return ResultUtil.data(refundLogService.getById(id));
    }

    @GetMapping
    @ApiOperation(value = "分页获取退款日志")
    public ResultMessage<IPage<RefundLog>> getByPage(RefundLog entity, SearchVO searchVo, PageVO page) {
        return ResultUtil.data(refundLogService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo)));
    }

}
