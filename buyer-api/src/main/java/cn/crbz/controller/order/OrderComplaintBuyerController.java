package cn.crbz.controller.order;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.AuthUser;
import cn.crbz.common.security.OperationalJudgment;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.order.entity.dos.OrderComplaint;
import cn.crbz.modules.order.order.entity.dto.OrderComplaintDTO;
import cn.crbz.modules.order.order.entity.enums.CommunicationOwnerEnum;
import cn.crbz.modules.order.order.entity.vo.OrderComplaintCommunicationVO;
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

import javax.validation.Valid;
import java.util.Objects;

/**
 * 买家端,交易投诉接口
 *
 * @author paulG
 * @since 2020/12/7
 **/
@RestController
@Api(tags = "买家端,交易投诉接口")
@RequestMapping("/buyer/order/complain")
public class OrderComplaintBuyerController {

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
        OrderComplaintVO orderComplaintVO = OperationalJudgment.judgment(orderComplaintService.getOrderComplainById(id));
        return ResultUtil.data(orderComplaintVO);
    }

    @ApiOperation(value = "分页获取")
    @GetMapping
    public ResultMessage<IPage<OrderComplaint>> get(OrderComplaintSearchParams searchParams, PageVO pageVO) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        searchParams.setMemberId(currentUser.getId());
        return ResultUtil.data(orderComplaintService.getOrderComplainByPage(searchParams, pageVO));

    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "添加交易投诉")
    @PostMapping
    public ResultMessage<OrderComplaint> add(@Valid OrderComplaintDTO orderComplaintDTO) {
        return ResultUtil.data(orderComplaintService.addOrderComplain(orderComplaintDTO));
    }

    @ApiOperation(value = "添加交易投诉对话")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "complainId", value = "投诉单ID", required = true, paramType = "query"),
            @ApiImplicitParam(name = "content", value = "内容", required = true, paramType = "query")
    })
    @PostMapping("/communication")
    public ResultMessage<OrderComplaintCommunicationVO> addCommunication(@RequestParam String complainId, @RequestParam String content) {
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        OrderComplaintCommunicationVO communicationVO = new OrderComplaintCommunicationVO(complainId, content, CommunicationOwnerEnum.BUYER.name(), currentUser.getNickName(), currentUser.getId());
        orderComplaintCommunicationService.addCommunication(communicationVO);
        return ResultUtil.data(communicationVO);
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "取消售后")
    @ApiImplicitParam(name = "id", value = "投诉单ID", required = true, paramType = "path")
    @PutMapping(value = "/status/{id}")
    public ResultMessage<Object> cancel(@PathVariable String id) {
        orderComplaintService.cancel(id);
        return ResultUtil.success();
    }


}
