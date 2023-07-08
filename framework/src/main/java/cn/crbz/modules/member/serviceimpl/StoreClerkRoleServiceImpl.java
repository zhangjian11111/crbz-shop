package cn.crbz.modules.member.serviceimpl;

import cn.crbz.modules.member.entity.dos.StoreClerkRole;
import cn.crbz.modules.member.mapper.StoreClerkRoleMapper;
import cn.crbz.modules.member.service.StoreClerkRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户权限业务层实现
 *
 * @author Chopper
 * @since 2020/11/17 3:52 下午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreClerkRoleServiceImpl extends ServiceImpl<StoreClerkRoleMapper, StoreClerkRole> implements StoreClerkRoleService {

    @Override
    public List<StoreClerkRole> listByUserId(String clerkId) {
        QueryWrapper<StoreClerkRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("clerk_id", clerkId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<String> listId(String clerkId) {
        List<StoreClerkRole> userRoleList = this.listByUserId(clerkId);
        List<String> strings = new ArrayList<>();
        userRoleList.forEach(item -> strings.add(item.getRoleId()));
        return strings;
    }

    @Override
    public void updateClerkRole(String clerkId, List<StoreClerkRole> storeClerkRoles) {
        //删除
        QueryWrapper<StoreClerkRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("clerk_id", clerkId);
        this.remove(queryWrapper);
        //保存
        this.saveBatch(storeClerkRoles);
    }

}
