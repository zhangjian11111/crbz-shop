package cn.crbz.modules.file.entity.dto;

import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.SearchVO;
import cn.crbz.modules.file.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileSearchParams extends PageVO {

    @ApiModelProperty(value = "文件")
    private File file;
    @ApiModelProperty(value = "搜索VO")
    private SearchVO searchVO;
    @ApiModelProperty(value = "文件夹ID")
    private String fileDirectoryId;
}
