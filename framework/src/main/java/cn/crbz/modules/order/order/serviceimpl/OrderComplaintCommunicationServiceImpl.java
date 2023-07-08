package cn.crbz.modules.order.order.serviceimpl;

import cn.crbz.mybatis.util.PageUtil;
import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.order.order.entity.dos.OrderComplaintCommunication;
import cn.crbz.modules.order.order.entity.vo.OrderComplaintCommunicationSearchParams;
import cn.crbz.modules.order.order.entity.vo.OrderComplaintCommunicationVO;
import cn.crbz.modules.order.order.mapper.OrderComplainCommunicationMapper;
import cn.crbz.modules.order.order.service.OrderComplaintCommunicationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 交易投诉通信业务层实现
 *
 * @author paulG
 * @since 2020/12/5
 **/
@Service
public class OrderComplaintCommunicationServiceImpl extends ServiceImpl<OrderComplainCommunicationMapper, OrderComplaintCommunication> implements OrderComplaintCommunicationService {

    @Override
    public boolean addCommunication(OrderComplaintCommunicationVO communicationVO) {
        return this.save(communicationVO);
    }

    @Override
    public IPage<OrderComplaintCommunication> getCommunication(OrderComplaintCommunicationSearchParams searchParams, PageVO pageVO) {
        return this.page(PageUtil.initPage(pageVO), searchParams.lambdaQueryWrapper());
    }
}
