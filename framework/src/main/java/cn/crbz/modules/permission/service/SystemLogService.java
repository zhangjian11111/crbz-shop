package cn.crbz.modules.permission.service;

import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.SearchVO;
import cn.crbz.modules.permission.entity.vo.SystemLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 系统日志业务层
 *
 * @author Chopper
 * @since 2020/11/17 3:45 下午
 */
public interface SystemLogService {

    /**
     * 添加日志
     *
     * @param systemLogVO
     * @return
     */
    void saveLog(SystemLogVO systemLogVO);

    /**
     * 通过id删除日志
     *
     * @param id
     */
    void deleteLog(List<String> id);

    /**
     * 删除全部日志
     */
    void flushAll();

    /**
     * 分页搜索获取日志
     *
     * @param key          关键字
     * @param searchVo     查询VO
     * @param pageVO       分页
     * @param operatorName 操作人
     * @param storeId      店铺ID
     * @return 日志分页
     */
    IPage<SystemLogVO> queryLog(String storeId, String operatorName, String key, SearchVO searchVo, PageVO pageVO);
}
