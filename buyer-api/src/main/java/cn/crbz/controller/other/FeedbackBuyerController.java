package cn.crbz.controller.other;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.page.entity.dos.Feedback;
import cn.crbz.modules.page.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 买家端,意见反馈接口
 *
 * @author Bulbasaur
 * @since 2020-05-5 15:10:16
 */
@RestController
@Api(tags = "买家端,意见反馈接口")
@RequestMapping("/buyer/other/feedback")
public class FeedbackBuyerController {

    /**
     * 意见反馈
     */
    @Autowired
    private FeedbackService feedbackService;

    @PreventDuplicateSubmissions
    @ApiOperation(value = "添加意见反馈")
    @PostMapping()
    public ResultMessage<Object> save(@Valid Feedback feedback) {
        feedback.setUserName(UserContext.getCurrentUser().getNickName());
        feedbackService.save(feedback);
        return ResultUtil.success();
    }

}
