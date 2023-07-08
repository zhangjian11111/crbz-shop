package cn.crbz.controller.other.broadcast;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.goods.entity.vos.StudioVO;
import cn.crbz.modules.goods.service.StudioService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,直播间接口
 *
 * @author Bulbasaur
 * @since 2021/5/20 12:03 下午
 */
@RestController
@Api(tags = "买家端,直播间接口")
@RequestMapping("/buyer/broadcast/studio")
public class StudioController {

    @Autowired
    private StudioService studioService;

    @ApiOperation(value = "获取店铺直播间列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recommend", value = "是否推荐", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "直播间状态", paramType = "query", dataType = "String")
    })
    @GetMapping
    public ResultMessage<IPage<StudioVO>> page(PageVO pageVO, Integer recommend, String status) {
        return ResultUtil.data(studioService.studioList(pageVO, recommend, status));
    }

    @ApiOperation(value = "获取店铺直播间回放地址")
    @GetMapping("/getLiveInfo/{roomId}")
    public ResultMessage<Object> getLiveInfo(Integer roomId) {
        return ResultUtil.data(studioService.getLiveInfo(roomId));
    }

}
