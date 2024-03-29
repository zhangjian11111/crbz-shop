package cn.crbz.controller.wallet;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.order.trade.entity.vo.RechargeQueryVO;
import cn.crbz.modules.wallet.entity.dos.Recharge;
import cn.crbz.modules.wallet.service.RechargeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,预存款充值记录接口
 *
 * @author pikachu
 * @since 2020/11/16 10:07 下午
 */
@RestController
@Api(tags = "买家端,预存款充值记录接口")
@RequestMapping("/buyer/wallet/recharge")
public class RechargeBuyerController {

    @Autowired
    private RechargeService rechargeService;

    @ApiOperation(value = "分页获取预存款充值记录")
    @GetMapping
    public ResultMessage<IPage<Recharge>> getByPage(PageVO page) {
        //构建查询参数
        RechargeQueryVO rechargeQueryVO = new RechargeQueryVO();
        rechargeQueryVO.setMemberId(UserContext.getCurrentUser().getId());
        //构建查询 返回数据
        IPage<Recharge> rechargePage = rechargeService.rechargePage(page, rechargeQueryVO);
        return ResultUtil.data(rechargePage);
    }
}
