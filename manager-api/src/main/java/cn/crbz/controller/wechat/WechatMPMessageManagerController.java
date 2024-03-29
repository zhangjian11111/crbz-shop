package cn.crbz.controller.wechat;

import cn.crbz.common.aop.annotation.DemoSite;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.common.vo.SearchVO;
import cn.crbz.modules.wechat.entity.dos.WechatMPMessage;
import cn.crbz.modules.wechat.service.WechatMPMessageService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Chopper
 */
@RestController
@Api(tags = "微信小程序消息订阅接口")
@RequestMapping("/manager/wechat/wechatMPMessage")
public class WechatMPMessageManagerController {
    @Autowired
    private WechatMPMessageService wechatMPMessageService;

    @DemoSite
    @GetMapping(value = "/init")
    @ApiOperation(value = "初始化微信小程序消息订阅")
    public ResultMessage init() {
        wechatMPMessageService.init();
        return ResultUtil.success();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查看微信小程序消息订阅详情")
    public ResultMessage<WechatMPMessage> get(@PathVariable String id) {

        WechatMPMessage wechatMPMessage = wechatMPMessageService.getById(id);
        return new ResultUtil<WechatMPMessage>().setData(wechatMPMessage);
    }

    @GetMapping
    @ApiOperation(value = "分页获取微信小程序消息订阅")
    public ResultMessage<IPage<WechatMPMessage>> getByPage(WechatMPMessage entity,
                                                           SearchVO searchVo,
                                                           PageVO page) {
        IPage<WechatMPMessage> data = wechatMPMessageService.page(PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo));
        return new ResultUtil<IPage<WechatMPMessage>>().setData(data);
    }

    @DemoSite
    @PostMapping
    @ApiOperation(value = "新增微信小程序消息订阅")
    public ResultMessage<WechatMPMessage> save(WechatMPMessage wechatMPMessage) {

        wechatMPMessageService.save(wechatMPMessage);
        return new ResultUtil<WechatMPMessage>().setData(wechatMPMessage);
    }

    @DemoSite
    @PutMapping("/{id}")
    @ApiOperation(value = "更新微信小程序消息订阅")
    public ResultMessage<WechatMPMessage> update(@PathVariable String id, WechatMPMessage wechatMPMessage) {
        wechatMPMessageService.updateById(wechatMPMessage);
        return new ResultUtil<WechatMPMessage>().setData(wechatMPMessage);
    }

    @DemoSite
    @DeleteMapping(value = "/{ids}")
    @ApiOperation(value = "删除微信小程序消息订阅")
    public ResultMessage<Object> delAllByIds(@PathVariable List ids) {

        wechatMPMessageService.removeByIds(ids);
        return ResultUtil.success();
    }
}
