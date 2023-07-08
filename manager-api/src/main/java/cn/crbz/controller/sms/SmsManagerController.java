package cn.crbz.controller.sms;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.sms.entity.dos.SmsReach;
import cn.crbz.modules.sms.service.SmsReachService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端,短信接口
 *
 * @author Bulbasaur
 * @since 2021/1/30 4:09 下午
 */
@RestController
@Api(tags = "管理端,短信接口")
@RequestMapping("/manager/sms/sms")
public class SmsManagerController {
    @Autowired
    private SmsReachService smsReachService;

    @ApiOperation(value = "接口批量发送短信")
    @PostMapping
    public ResultMessage<Object> sendBatchSms(SmsReach smsReach, @RequestParam(value = "mobile") List<String> mobile) {
        smsReachService.addSmsReach(smsReach,mobile);
        return ResultUtil.success();
    }

    @ApiOperation(value = "查询短信任务分页")
    @GetMapping()
    public ResultMessage<IPage<SmsReach>> querySmsReachPage(PageVO page) {
        return ResultUtil.data(smsReachService.page(PageUtil.initPage(page)));
    }

    @ApiOperation(value = "查询短信任务")
    @ApiImplicitParam(name = "id", value = "短信任务id", required = true, dataType = "String", allowMultiple = true, paramType = "path")
    @GetMapping("/{id}")
    public ResultMessage<SmsReach> querySmsReach(@PathVariable String id) {
        return ResultUtil.data(smsReachService.getById(id));
    }

}
