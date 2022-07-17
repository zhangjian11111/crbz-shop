package com.itheima.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.domain.dto.TimeDto;
import com.itheima.domain.ZbforcoshineEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 *
 * @author zhangjian
 * @email 1097951193@qq.com
 * @date 2022-05-18 22:27:09
 */
public interface ZbforcoshineService extends IService<ZbforcoshineEntity> {
    IPage<ZbforcoshineEntity> getPage(int currentPage, int pageSize, TimeDto timeDto);

//    PageUtils queryPage(Map<String, Object> params);

    /**导出周报
     *
     * @param starttime
     * @param endtime
     * @return
     */
    List<ZbforcoshineEntity> getByDate(TimeDto timeDto);

    void exportZb(HttpServletResponse response, String filename, List<ZbforcoshineEntity> list);

}

