package cn.crbz.controller.other;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.search.service.EsGoodsIndexService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ElasticsearchController
 *
 * @author Chopper
 * @version v1.0
 * 2021-03-24 18:32
 */
@RestController
@Api(tags = "ES初始化接口")
@RequestMapping("/manager/other/elasticsearch")
public class ElasticsearchController {

    @Autowired
    private EsGoodsIndexService esGoodsIndexService;

    @GetMapping
    public ResultMessage<String> init() {
        esGoodsIndexService.init();
        return ResultUtil.success();
    }

    @GetMapping("/progress")
    public ResultMessage<Map<String, Long>> getProgress() {
        return ResultUtil.data(esGoodsIndexService.getProgress());
    }
}
