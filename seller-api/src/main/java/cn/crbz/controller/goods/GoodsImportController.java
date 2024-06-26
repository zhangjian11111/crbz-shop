package cn.crbz.controller.goods;

import cn.crbz.common.context.ThreadContextHolder;
import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.goods.service.GoodsImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chc
 * @since 2022/6/2114:46
 */
@Api(tags = "商品导入")
@RestController
@RequestMapping("/store/goods/import")
public class GoodsImportController {
    @Autowired
    private GoodsImportService goodsImportService;


    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传文件，商品批量添加")
    public ResultMessage<Object> importExcel(@RequestPart("files") MultipartFile files) throws Exception {
        goodsImportService.importExcel(files);
        return ResultUtil.success(ResultCode.SUCCESS);
    }


    @ApiOperation(value = "下载导入模板", produces = "application/octet-stream")
    @GetMapping(value = "/downLoad")
    public void download() {
        HttpServletResponse response = ThreadContextHolder.getHttpResponse();

        goodsImportService.download(response);
    }
}
