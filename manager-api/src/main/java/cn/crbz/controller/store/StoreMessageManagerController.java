package cn.crbz.controller.store;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.message.entity.dos.StoreMessage;
import cn.crbz.modules.message.entity.vos.StoreMessageQueryVO;
import cn.crbz.modules.message.service.StoreMessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理端,店铺消息消息管理接口
 *
 * @author pikachu
 * @since 2020/12/6 16:09
 */
@RestController
@Api(tags = "管理端,店铺消息消息管理接口")
@RequestMapping("/manager/other/storeMessage")
public class StoreMessageManagerController {

    @Autowired
    private StoreMessageService storeMessageService;

    @GetMapping
    @ApiOperation(value = "多条件分页获取")
    public ResultMessage<IPage<StoreMessage>> getByCondition(StoreMessageQueryVO storeMessageQueryVO,
                                                             PageVO pageVo) {
        IPage<StoreMessage> page = storeMessageService.getPage(storeMessageQueryVO, pageVo);
        return ResultUtil.data(page);
    }

}
