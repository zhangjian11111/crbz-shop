package cn.crbz.controller.store.seat;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.im.entity.vo.SeatVO;
import cn.crbz.modules.im.service.SeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SeatController
 *
 * @author Chopper
 * @version v1.0
 * 2022-02-10 11:50
 */
@RestController
@Api(tags = "店铺端,坐席管理")
@RequestMapping("/store/seat/setting")
@Transactional(rollbackFor = Exception.class)
public class SeatStoreController {


    @Autowired
    private SeatService seatService;

    @ApiOperation(value = "分页获取坐席")
    @GetMapping("/list")
    public ResultMessage<List<SeatVO>> getSeats() {
        return ResultUtil.data(seatService.seatVoList(UserContext.getCurrentUser().getTenantId()));
    }


}
