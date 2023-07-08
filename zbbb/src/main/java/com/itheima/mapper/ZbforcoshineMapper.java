package com.itheima.mapper;

import com.itheima.domain.ZbforcoshineEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 *
 *
 * @author zhangjian
 * @email 1097951193@qq.com
 * @date 2022-05-18 22:27:09
 */
@Mapper
public interface ZbforcoshineMapper extends BaseMapper<ZbforcoshineEntity> {

    List<HashMap<Object, Object>> weGet();

}
