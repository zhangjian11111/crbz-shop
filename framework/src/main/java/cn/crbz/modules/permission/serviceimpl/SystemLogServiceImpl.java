package cn.crbz.modules.permission.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.common.vo.SearchVO;
import cn.crbz.modules.permission.entity.vo.SystemLogVO;
import cn.crbz.modules.permission.repository.SystemLogRepository;
import cn.crbz.modules.permission.service.SystemLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统日志
 *
 * @author Chopper
 * @since 2020/11/17 3:45 下午
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogRepository systemLogRepository;

    /**
     * ES
     */
    @Autowired
    private ElasticsearchOperations restTemplate;

    @Override
    public void saveLog(SystemLogVO systemLogVO) {
        systemLogRepository.save(systemLogVO);
    }

    @Override
    public void deleteLog(List<String> id) {
        for (String s : id) {
            systemLogRepository.deleteById(s);
        }
    }

    @Override
    public void flushAll() {
        systemLogRepository.deleteAll();
    }

    @Override
    public IPage<SystemLogVO> queryLog(String storeId, String operatorName, String key, SearchVO searchVo, PageVO pageVO) {
        pageVO.setNotConvert(true);
        IPage<SystemLogVO> iPage = new Page<>();
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        if (pageVO.getPageNumber() != null && pageVO.getPageSize() != null) {
            int pageNumber = pageVO.getPageNumber() - 1;
            if (pageNumber < 0) {
                pageNumber = 0;
            }
            Pageable pageable = PageRequest.of(pageNumber, pageVO.getPageSize());
            //分页
            nativeSearchQueryBuilder.withPageable(pageable);
            iPage.setCurrent(pageVO.getPageNumber());
            iPage.setSize(pageVO.getPageSize());
        }

        if (CharSequenceUtil.isNotEmpty(storeId)) {
            nativeSearchQueryBuilder.withFilter(QueryBuilders.matchQuery("storeId", storeId));
        }

        if (CharSequenceUtil.isNotEmpty(operatorName)) {
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("username", operatorName));
        }

        if (CharSequenceUtil.isNotEmpty(key)) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(key, "requestUrl", "requestParam", "responseBody", "name");
            multiMatchQueryBuilder.fuzziness(Fuzziness.AUTO);
            nativeSearchQueryBuilder.withFilter(multiMatchQueryBuilder);
        }
        //时间有效性判定
        if (searchVo.getConvertStartDate() != null && searchVo.getConvertEndDate() != null) {
            BoolQueryBuilder filterBuilder = new BoolQueryBuilder();
            //大于方法
            filterBuilder.filter(
                    QueryBuilders.rangeQuery("createTime")
                            .gte(searchVo.getConvertStartDate().getTime())
                            .lte(searchVo.getConvertEndDate().getTime()));

            nativeSearchQueryBuilder.withFilter(filterBuilder);
        }

        if (CharSequenceUtil.isNotEmpty(pageVO.getOrder()) && CharSequenceUtil.isNotEmpty(pageVO.getSort())) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(pageVO.getSort()).order(SortOrder.valueOf(pageVO.getOrder().toUpperCase())));
        } else {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        }

        SearchHits<SystemLogVO> searchResult = restTemplate.search(nativeSearchQueryBuilder.build(), SystemLogVO.class);

        iPage.setTotal(searchResult.getTotalHits());

        iPage.setRecords(searchResult.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()));
        return iPage;
    }

}
