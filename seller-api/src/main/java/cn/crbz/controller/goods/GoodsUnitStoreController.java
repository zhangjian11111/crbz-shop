package cn.crbz.controller.goods;


import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.goods.entity.dos.GoodsUnit;
import cn.crbz.modules.goods.service.GoodsUnitService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,商品计量单位接口
 *
 * @author Bulbasaur
 * @since 2020/11/26 16:15
 */
@RestController
@Api(tags = "店铺端,商品计量单位接口")
@RequestMapping("/store/goods/goodsUnit")
public class GoodsUnitStoreController {
    @Autowired
    private GoodsUnitService goodsUnitService;


    @ApiOperation(value = "分页获取商品计量单位")
    @GetMapping
    public ResultMessage<IPage<GoodsUnit>> getByPage(PageVO pageVO) {
        return ResultUtil.data(goodsUnitService.page(PageUtil.initPage(pageVO)));
    }


}
