package cn.crbz.modules.page.serviceimpl;


import cn.crbz.modules.page.entity.dos.Feedback;
import cn.crbz.modules.page.mapper.FeedbackMapper;
import cn.crbz.modules.page.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 意见反馈业务层实现
 *
 * @author Chopper
 * @since 2020/11/18 11:40 上午
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

}
