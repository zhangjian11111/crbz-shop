package cn.crbz.controller.other;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.permission.SettingKeys;
import cn.crbz.modules.search.entity.dos.CustomWords;
import cn.crbz.modules.search.entity.vo.CustomWordsVO;
import cn.crbz.modules.search.service.CustomWordsService;
import cn.crbz.modules.system.entity.dos.Setting;
import cn.crbz.modules.system.service.SettingService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

/**
 * 管理端,自定义分词接口
 *
 * @author paulG
 * @since 2020/10/16
 **/
@Slf4j
@RestController
@Api(tags = "管理端,自定义分词接口")
@RequestMapping("/manager/other/customWords")
public class CustomWordsController {

    /**
     * 分词
     */
    @Autowired
    private CustomWordsService customWordsService;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;

    @GetMapping
    public String getCustomWords(String secretKey) {
        if (CharSequenceUtil.isEmpty(secretKey)) {
            return "";
        }
        Setting setting = settingService.get(SettingKeys.ES_SIGN.name());
        if (setting == null || CharSequenceUtil.isEmpty(setting.getSettingValue())) {
            return "";
        }

        JSONObject jsonObject = JSONUtil.parseObj(setting.getSettingValue());
        //如果密钥不正确，返回空
        if (!secretKey.equals(jsonObject.get("secretKey"))) {
            return "";
        }

        String res = customWordsService.deploy();
        try {
            return new String(res.getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("获取分词错误", e);
        }
        return "";
    }

    @ApiOperation(value = "添加自定义分词")
    @PostMapping
    public ResultMessage<CustomWordsVO> addCustomWords(@Valid CustomWordsVO customWords) {
        customWordsService.addCustomWords(customWords);
        return ResultUtil.data(customWords);
    }

    @ApiOperation(value = "修改自定义分词")
    @PutMapping
    public ResultMessage<CustomWordsVO> updateCustomWords(@Valid CustomWordsVO customWords) {
        customWordsService.updateCustomWords(customWords);
        return ResultUtil.data(customWords);
    }

    @ApiOperation(value = "删除自定义分词")
    @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/{id}")
    public ResultMessage<String> deleteCustomWords(@NotNull @PathVariable String id) {
        customWordsService.deleteCustomWords(id);
        return ResultUtil.success();
    }

    @ApiOperation(value = "分页获取自定义分词")
    @ApiImplicitParam(name = "words", value = "分词", required = true, dataType = "String", paramType = "query")
    @GetMapping("/page")
    public ResultMessage<IPage<CustomWords>> getCustomWords(@RequestParam String words, PageVO pageVo) {
        return ResultUtil.data(customWordsService.getCustomWordsByPage(words, pageVo));
    }

}
