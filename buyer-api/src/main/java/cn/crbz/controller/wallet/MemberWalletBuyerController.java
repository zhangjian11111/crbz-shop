package cn.crbz.controller.wallet;

import cn.crbz.common.aop.annotation.PreventDuplicateSubmissions;
import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.common.security.AuthUser;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.dos.Member;
import cn.crbz.modules.member.service.MemberService;
import cn.crbz.modules.system.entity.dos.Setting;
import cn.crbz.modules.system.entity.dto.WithdrawalSetting;
import cn.crbz.modules.system.entity.enums.SettingEnum;
import cn.crbz.modules.system.entity.vo.WithdrawalSettingVO;
import cn.crbz.modules.system.service.SettingService;
import cn.crbz.modules.verification.entity.enums.VerificationEnums;
import cn.crbz.modules.verification.service.VerificationService;
import cn.crbz.modules.wallet.entity.dos.MemberWallet;
import cn.crbz.modules.wallet.entity.vo.MemberWalletVO;
import cn.crbz.modules.wallet.service.MemberWalletService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

/**
 * 买家端,会员余额接口
 *
 * @author pikachu
 * @since 2020/11/16 10:07 下午
 */
@RestController
@Api(tags = "买家端,会员余额接口")
@RequestMapping("/buyer/wallet/wallet")
@Validated
public class MemberWalletBuyerController {

    /**
     * 会员
     */
    @Autowired
    private MemberService memberService;
    /**
     * 会员余额
     */
    @Autowired
    private MemberWalletService memberWalletService;
    /**
     * 验证码
     */
    @Autowired
    private VerificationService verificationService;

    @Autowired
    private SettingService settingService;

    @GetMapping
    @ApiOperation(value = "查询会员预存款余额")
    public ResultMessage<MemberWalletVO> get() {
        AuthUser authUser = UserContext.getCurrentUser();
        if (authUser != null) {
            return ResultUtil.data(memberWalletService.getMemberWallet(authUser.getId()));
        }
        throw new ServiceException(ResultCode.USER_NOT_LOGIN);
    }

    @GetMapping(value = "/withdrawalSettingVO")
    @ApiOperation(value = "获取提现设置VO")
    public ResultMessage<Object> minPrice() {
        Setting setting = settingService.get(SettingEnum.WITHDRAWAL_SETTING.name());
        WithdrawalSetting withdrawalSetting = new Gson().fromJson(setting.getSettingValue(), WithdrawalSetting.class);

        WithdrawalSettingVO withdrawalSettingVO = new WithdrawalSettingVO();
        withdrawalSettingVO.setMinPrice(withdrawalSetting.getMinPrice());
        withdrawalSettingVO.setFee(withdrawalSetting.getFee());
        withdrawalSettingVO.setType(withdrawalSetting.getType());
        return ResultUtil.data(withdrawalSettingVO);
    }

    @PreventDuplicateSubmissions
    @PostMapping(value = "/withdrawal")
    @ApiOperation(value = "会员中心余额提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "price", value = "提现金额", required = true, dataType = "double", paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "真实姓名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "connectNumber", value = "第三方登录账号", required = true, dataType = "String", paramType = "query")
    })
    public ResultMessage<Boolean> withdrawal(@Max(value = 9999, message = "充值金额单次最多允许提现9999元") Double price, @RequestParam String realName, @RequestParam String connectNumber) {
        return ResultUtil.data(memberWalletService.applyWithdrawal(price, realName, connectNumber));
    }

    @PostMapping(value = "/set-password")
    @ApiOperation(value = "设置支付密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "支付密码", required = true, dataType = "String", paramType = "query")
    })
    public ResultMessage<Object> setPassword(String password, @RequestHeader String uuid) {
        AuthUser authUser = UserContext.getCurrentUser();
        //校验当前用户是否存在
        Member member = memberService.getById(authUser.getId());
        if (member == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        //校验验证码
        if (verificationService.check(uuid, VerificationEnums.WALLET_PASSWORD)) {
            memberWalletService.setMemberWalletPassword(member, password);
            throw new ServiceException(ResultCode.SUCCESS);
        } else {
            throw new ServiceException(ResultCode.VERIFICATION_ERROR);
        }

    }

    @PostMapping(value = "/update-password/ordinary")
    @ApiOperation(value = "普通方式进行支付密码的修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧支付密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "newPassword", value = "新支付密码", required = true, dataType = "String", paramType = "query")
    })
    public ResultMessage updatePassword(@RequestParam @Pattern(regexp = "[a-fA-F0-9]{32}", message = "旧密码格式不正确") String oldPassword,
                                        @RequestParam @Pattern(regexp = "[a-fA-F0-9]{32}", message = "新密码格式不正确") String newPassword) {
        AuthUser authUser = UserContext.getCurrentUser();
        //校验当前用户是否存在
        Member member = memberService.getById(authUser.getId());
        if (member == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        MemberWallet memberWallet = this.memberWalletService.getOne(new QueryWrapper<MemberWallet>().eq("member_id", member.getId()));
        //校验新旧密码是否一致
        if (memberWallet != null) {
            if (!new BCryptPasswordEncoder().matches(oldPassword, memberWallet.getWalletPassword())) {
                throw new ServiceException(ResultCode.USER_OLD_PASSWORD_ERROR);
            }
            this.memberWalletService.setMemberWalletPassword(member, newPassword);
            return ResultUtil.data("修改成功");
        } else {
            throw new ServiceException(ResultCode.WALLET_NOT_EXIT_ERROR);
        }
    }


    @GetMapping(value = "/check")
    @ApiOperation(value = "检测会员是否设置过支付密码,会员中心设置或者修改密码时使用")
    public Boolean checkPassword() {
        return memberWalletService.checkPassword();
    }

}
