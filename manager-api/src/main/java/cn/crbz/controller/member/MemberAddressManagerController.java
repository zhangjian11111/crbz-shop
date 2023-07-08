package cn.crbz.controller.member;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.dos.MemberAddress;
import cn.crbz.modules.member.service.MemberAddressService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理端,会员地址API
 *
 * @author Bulbasaur
 * @since 2020-02-25 14:10:16
 */
@RestController
@Api(tags = "管理端,会员地址API")
@RequestMapping("/manager/member/address")
public class MemberAddressManagerController {
    @Autowired
    private MemberAddressService memberAddressService;

    @ApiOperation(value = "会员地址分页列表")
    @GetMapping("/{memberId}")
    public ResultMessage<IPage<MemberAddress>> getByPage(PageVO page, @PathVariable("memberId") String memberId) {
        return ResultUtil.data(memberAddressService.getAddressByMember(page, memberId));
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "删除会员收件地址")
    @ApiImplicitParam(name = "id", value = "会员地址ID", dataType = "String", paramType = "path")
    @DeleteMapping(value = "/delById/{id}")
    public ResultMessage<Object> delShippingAddressById(@PathVariable String id) {
        memberAddressService.removeMemberAddress(id);
        return ResultUtil.success();
    }

    @ApiOperation(value = "修改会员收件地址")
    @PutMapping
    public ResultMessage<MemberAddress> editShippingAddress(@Valid MemberAddress shippingAddress) {
        //修改会员地址
        return ResultUtil.data(memberAddressService.updateMemberAddress(shippingAddress));
    }

    @PreventDuplicateSubmissions
    @ApiOperation(value = "新增会员收件地址")
    @PostMapping
    public ResultMessage<MemberAddress> addShippingAddress(@Valid MemberAddress shippingAddress) {
        //添加会员地址
        return ResultUtil.data(memberAddressService.saveMemberAddress(shippingAddress));
    }


}
