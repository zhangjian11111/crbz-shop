package cn.crbz.modules.wallet.service;

import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.order.trade.entity.vo.DepositQueryVO;
import cn.crbz.modules.wallet.entity.dos.WalletLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 钱包变动日志业务层
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
public interface WalletLogService extends IService<WalletLog> {


    /**
     * 预存款充值日志记录
     *
     * @param page           分页数据
     * @param depositQueryVO 查询条件
     * @return 日志记录分页列表
     */
    IPage<WalletLog> depositLogPage(PageVO page, DepositQueryVO depositQueryVO);

}
