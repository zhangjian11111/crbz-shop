package cn.crbz.modules.search.mapper;

import cn.crbz.modules.search.entity.dos.CustomWords;
import cn.crbz.mybatis.mybatisplus.external.SpiceBaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 自定义分词数据处理层
 *
 * @author paulG
 * @since 2020/10/15
 **/
public interface CustomWordsMapper extends BaseMapper<CustomWords>, SpiceBaseMapper<CustomWords> {
}
