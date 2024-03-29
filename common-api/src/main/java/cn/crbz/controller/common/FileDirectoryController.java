package cn.crbz.controller.common;

import cn.crbz.common.enums.ResultUtil;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.ResultMessage;
import cn.crbz.modules.file.entity.FileDirectory;
import cn.crbz.modules.file.entity.dto.FileDirectoryDTO;
import cn.crbz.modules.file.service.FileDirectoryService;
import cn.crbz.modules.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件目录管理接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@RestController
@Api(tags = "文件目录管理接口")
@RequestMapping("/common/resource/fileDirectory")
@RequiredArgsConstructor
public class FileDirectoryController {

    private final FileDirectoryService fileDirectoryService;
    private final FileService fileService;

    @ApiOperation(value = "获取文件目录列表")
    @GetMapping
    public ResultMessage<List<FileDirectoryDTO>> getSceneFileList() {
        return ResultUtil.data(fileDirectoryService.getFileDirectoryList(UserContext.getCurrentUser().getId()));
    }

    @ApiOperation(value = "添加文件目录")
    @PostMapping
    public ResultMessage<FileDirectory> addSceneFileList(@RequestBody FileDirectory fileDirectory) {
        fileDirectory.setDirectoryType(UserContext.getCurrentUser().getRole().name());
        fileDirectoryService.save(fileDirectory);
        return ResultUtil.data(fileDirectory);
    }

    @ApiOperation(value = "修改文件目录")
    @PutMapping
    public ResultMessage<FileDirectory> editSceneFileList(@RequestBody FileDirectory fileDirectory) {
        fileDirectory.setDirectoryType(UserContext.getCurrentUser().getRole().name());
        fileDirectoryService.updateById(fileDirectory);
        return ResultUtil.data(fileDirectory);
    }

    @ApiOperation(value = "删除文件目录")
    @DeleteMapping("/{id}")
    public ResultMessage<Object> deleteSceneFileList(@PathVariable String id) {
        //删除文件夹下面的图片
        fileService.batchDeleteByDirectory(id);
        //删除目录
        fileDirectoryService.removeById(id);
        return ResultUtil.success();
    }

}
