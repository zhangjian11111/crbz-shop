package cn.crbz.modules.message.serviceimpl;

import cn.crbz.modules.message.entity.dos.ShortLink;
import cn.crbz.modules.message.mapper.ShortLinkMapper;
import cn.crbz.modules.message.service.ShortLinkService;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链接 业务实现
 *
 * @author Chopper
 */
@Service
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLink> implements ShortLinkService {

    @Override
    public List<ShortLink> queryShortLinks(ShortLink shortLink) {
        return this.list(PageUtil.initWrapper(shortLink));
    }
}
