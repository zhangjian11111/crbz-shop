package cn.crbz.controller.order;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.AuthUser;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.order.entity.dos.OrderComplaint;
import cn.crbz.modules.order.order.entity.enums.CommunicationOwnerEnum;
import cn.crbz.modules.order.order.entity.enums.OrderComplaintStatusEnum;
import cn.crbz.modules.order.order.entity.vo.OrderComplaintCommunicationVO;
import cn.crbz.modules.order.order.entity.vo.OrderComplaintOperationParams;
import cn.crbz.modules.order.order.entity.vo.OrderComplaintSearchParams;
import cn.crbz.modules.order.order.entity.vo.OrderComplaintVO;
import cn.crbz.modules.order.order.service.OrderComplaintCommunicationService;
import cn.crbz.modules.order.order.service.OrderComplaintService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 管理端,交易投诉接口
 *
 * @author paulG
 * @since 2020/12/5
 */
@RestController
@Api(tags = "管理端,交易投诉接口")
@RequestMapping("/manager/order/complain")
public class OrderComplaintManagerController {

    /**
     * 交易投诉
     */
    @Autowired
    private OrderComplaintService orderComplaintService;

    /**
     * 交易投诉沟通
     */
    @Autowired
    private OrderComplaintCommunicationService orderComplaintCommunicationService;

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "投诉单ID", required = true, paramType = "path")
    @GetMapping(value = "/{id}")
    public ResultMessage<OrderComplaintVO> get(@PathVariable String id) {
        return ResultUtil.data(orderComplaintService.getOrderComplainById(id));
    }

    @ApiOperation(value = "分页获取")
    @GetMapping
    public ResultMessage<IPage<OrderComplaint>> get(OrderComplaintSearchParams searchParams, PageVO pageVO) {
        return ResultUtil.data(orderComplaintService.getOrderComplainByPage(searchParams, pageVO));
    }

    @ApiOperation(value = "更新数据")
    @PutMapping
    public ResultMessage<OrderComplaintVO> update(OrderComplaintVO orderComplainVO) {
        orderComplaintService.updateOrderComplain(orderComplainVO);
        return ResultUtil.data(orderComplainVO);

    }

    @ApiOperation(value = "添加交易投诉对话")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "complainId", value = "投诉单ID", required = true, paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, paramType = "query")
    })
    @PostMapping("/communication")
    public ResultMessage<OrderComplaintCommunicationVO> addCommunication(@RequestParam String complainId, @RequestParam String content) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        OrderComplaintCommunicationVO communicationVO = new OrderComplaintCommunicationVO(complainId, content, CommunicationOwnerEnum.PLATFORM.name(), currentUser.getUsername(), currentUser.getId());
        orderComplaintCommunicationService.addCommunication(communicationVO);
        return ResultUtil.data(communicationVO);
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "修改状态")
    @PutMapping(value = "/status")
    public ResultMessage<Object> updateStatus(OrderComplaintOperationParams orderComplainVO) {
        orderComplaintService.updateOrderComplainByStatus(orderComplainVO);
        return ResultUtil.success();
    }


    @PreventDuplicateSubmissions
    @ApiOperation(value = "仲裁")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "投诉单ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "arbitrationResult", value = "仲裁结果", required = true, paramType = "query")
    })
    @PutMapping(value = "/complete/{id}")
    public ResultMessage<Object> complete(@PathVariable String id, String arbitrationResult) {
        //新建对象
        OrderComplaintOperationParams orderComplaintOperationParams = new OrderComplaintOperationParams();
        orderComplaintOperationParams.setComplainId(id);
        orderComplaintOperationParams.setArbitrationResult(arbitrationResult);
        orderComplaintOperationParams.setComplainStatus(OrderComplaintStatusEnum.COMPLETE.name());

        //修改状态
        orderComplaintService.updateOrderComplainByStatus(orderComplaintOperationParams);
        return ResultUtil.success();
    }
}
