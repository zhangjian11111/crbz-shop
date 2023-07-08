package cn.crbz.modules.system.serviceimpl;

import cn.crbz.modules.system.entity.dos.ServiceNotice;
import cn.crbz.modules.system.mapper.ServiceNoticeMapper;
import cn.crbz.modules.system.service.ServiceNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 服务订阅消息业务层实现
 * @author Chopper
 * @since 2020/11/17 8:02 下午
 */
@Service
public class ServiceNoticeServiceImpl extends ServiceImpl<ServiceNoticeMapper, ServiceNotice> implements ServiceNoticeService {

}
