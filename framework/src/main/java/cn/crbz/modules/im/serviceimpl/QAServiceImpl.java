package cn.crbz.modules.im.serviceimpl;

import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.im.entity.dos.QA;
import cn.crbz.modules.im.mapper.QAMapper;
import cn.crbz.modules.im.service.QAService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 坐席业务层实现
 *
 * @author pikachu
 * @since 2020-02-18 16:18:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QAServiceImpl extends ServiceImpl<QAMapper, QA> implements QAService {

    @Override
    public IPage<QA> getStoreQA(String word, PageVO pageVo) {
        LambdaQueryWrapper<QA> qaLambdaQueryWrapper = new LambdaQueryWrapper<>();
        qaLambdaQueryWrapper.eq(QA::getTenantId, UserContext.getCurrentUser().getTenantId());
        qaLambdaQueryWrapper.like(QA::getQuestion, word);
        return this.page(PageUtil.initPage(pageVo), qaLambdaQueryWrapper);
    }
}
