package cn.crbz.modules.permission.repository;

import cn.crbz.modules.permission.entity.vo.SystemLogVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 日志
 *
 * @author paulG
 * @since 2021/12/13
 **/
public interface SystemLogRepository extends ElasticsearchRepository<SystemLogVO, String> {

}
