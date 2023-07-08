package cn.crbz.controller.member;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.member.entity.dto.FootPrintQueryParams;
import cn.crbz.modules.member.service.FootprintService;
import cn.crbz.modules.search.entity.dos.EsGoodsIndex;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家端,浏览历史接口
 *
 * @author chc
 * @since 2022/6/2114:46
 */
@RestController
@Api(tags = "商家端,浏览历史接口")
@RequestMapping("/store/member/footprint")
public class FootprintStoreController {

    /**
     * 会员足迹
     */
    @Autowired
    private FootprintService footprintService;

    @ApiOperation(value = "分页获取")
    @GetMapping
    public ResultMessage<IPage<EsGoodsIndex>> getByPage(FootPrintQueryParams params) {
        return ResultUtil.data(footprintService.footPrintPage(params));
    }
}
