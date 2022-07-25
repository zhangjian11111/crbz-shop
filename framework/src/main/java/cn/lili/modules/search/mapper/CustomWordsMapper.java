package cn.lili.modules.search.mapper;

import cn.lili.modules.search.entity.dos.CustomWords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.lili.mybatis.mybatisplus.external.SpiceBaseMapper;

/**
 * 自定义分词数据处理层
 *
 * @author paulG
 * @since 2020/10/15
 **/
public interface CustomWordsMapper extends BaseMapper<CustomWords>, SpiceBaseMapper<CustomWords>{

}
