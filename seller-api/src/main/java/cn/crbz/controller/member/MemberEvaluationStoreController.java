package cn.crbz.controller.member;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.OperationalJudgment;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.dto.EvaluationQueryParams;
import cn.crbz.modules.member.entity.vo.MemberEvaluationListVO;
import cn.crbz.modules.member.entity.vo.MemberEvaluationVO;
import cn.crbz.modules.member.service.MemberEvaluationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 店铺端,商品评价管理接口
 *
 * @author Bulbasaur
 * @since 2020-02-25 14:10:16
 */
@RestController
@Api(tags = "店铺端,商品评价管理接口")
@RequestMapping("/store/member/evaluation")
public class MemberEvaluationStoreController {

    @Autowired
    private MemberEvaluationService memberEvaluationService;

    @ApiOperation(value = "分页获取会员评论列表")
    @GetMapping
    public ResultMessage<IPage<MemberEvaluationListVO>> getByPage(EvaluationQueryParams evaluationQueryParams) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        evaluationQueryParams.setStoreId(storeId);
        return ResultUtil.data(memberEvaluationService.queryPage(evaluationQueryParams));
    }

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "评价ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/get/{id}")
    public ResultMessage<MemberEvaluationVO> get(@PathVariable String id) {
        return ResultUtil.data(OperationalJudgment.judgment(memberEvaluationService.queryById(id)));
    }

    @ApiOperation(value = "回复评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "reply", value = "回复内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "replyImage", value = "回复图片", dataType = "String", paramType = "query")
    })
    @PutMapping(value = "/reply/{id}")
    public ResultMessage<MemberEvaluationVO> reply(@PathVariable String id, @RequestParam String reply, @RequestParam String replyImage) {
        OperationalJudgment.judgment(memberEvaluationService.queryById(id));
        memberEvaluationService.reply(id, reply, replyImage);
        return ResultUtil.success();
    }
}
