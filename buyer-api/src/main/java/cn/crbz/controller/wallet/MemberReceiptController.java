package cn.crbz.controller.wallet;


import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.vo.MemberReceiptAddVO;
import cn.crbz.modules.member.entity.vo.MemberReceiptVO;
import cn.crbz.modules.member.service.MemberReceiptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 买家端,会员发票接口
 *
 * @author paulG
 * @since 2021-03-29 14:10:16
 */
@RestController
@Api(tags = "买家端,会员发票接口")
@RequestMapping("/buyer/wallet/receipt")
public class MemberReceiptController {

    @Autowired
    private MemberReceiptService memberReceiptService;

    @ApiOperation(value = "查询会员发票列表")
    @GetMapping
    public ResultMessage<Object> page(MemberReceiptVO memberReceiptVO, PageVO page) {
        return ResultUtil.data(memberReceiptService.getPage(memberReceiptVO, page));
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "新增会员发票")
    @PostMapping
    public ResultMessage<Object> add(MemberReceiptAddVO memberReceiptAddVO) {
        return ResultUtil.data(memberReceiptService.addMemberReceipt(memberReceiptAddVO, UserContext.getCurrentUser().getId()));
    }

    @ApiOperation(value = "修改会员发票")
    @ApiImplicitParam(name = "id", value = "会员发票id", required = true, paramType = "path")
    @PutMapping
    public ResultMessage<Object> update(@PathVariable String id, MemberReceiptAddVO memberReceiptAddVO) {
        memberReceiptAddVO.setId(id);
        return ResultUtil.data(memberReceiptService.editMemberReceipt(memberReceiptAddVO, id));
    }

    @ApiOperation(value = "会员发票删除")
    @ApiImplicitParam(name = "id", value = "会员发票id", required = true, paramType = "path")
    @DeleteMapping
    public ResultMessage<Boolean> deleteMessage(@PathVariable String id) {
        return ResultUtil.data(memberReceiptService.deleteMemberReceipt(id));
    }

}
