package cn.crbz.modules.goods.serviceimpl;

import cn.crbz.modules.goods.entity.dos.GoodsWords;
import cn.crbz.modules.goods.mapper.GoodsWordsMapper;
import cn.crbz.modules.goods.service.GoodsWordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商品关键字业务层实现
 *
 * @author paulG
 * @since 2020/10/15
 **/
@Service
public class GoodsWordsServiceImpl extends ServiceImpl<GoodsWordsMapper, GoodsWords> implements GoodsWordsService {
}
