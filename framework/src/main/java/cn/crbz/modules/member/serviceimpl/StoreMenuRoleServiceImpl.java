package cn.crbz.modules.member.serviceimpl;

import cn.crbz.cache.Cache;
import cn.crbz.cache.CachePrefix;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.security.enums.UserEnums;
import cn.crbz.modules.member.entity.dos.StoreMenuRole;
import cn.crbz.modules.member.entity.vo.StoreUserMenuVO;
import cn.crbz.modules.member.mapper.StoreMenuRoleMapper;
import cn.crbz.modules.member.service.StoreMenuRoleService;
import cn.crbz.modules.member.service.StoreMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色菜单业务层实现
 *
 * @author Chopper
 * @since 2020/11/22 11:43
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreMenuRoleServiceImpl extends ServiceImpl<StoreMenuRoleMapper, StoreMenuRole> implements StoreMenuRoleService {

    /**
     * 菜单
     */
    @Autowired
    private StoreMenuService storeMenuService;

    @Autowired
    private Cache<Object> cache;

    @Override
    public List<StoreMenuRole> findByRoleId(String roleId) {
        LambdaQueryWrapper<StoreMenuRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreMenuRole::getRoleId, roleId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<StoreUserMenuVO> findAllMenu(String clerkId, String memberId) {
        String cacheKey = CachePrefix.STORE_USER_MENU.getPrefix() + memberId;
        List<StoreUserMenuVO> menuList = (List<StoreUserMenuVO>) cache.get(cacheKey);
        if (menuList == null || menuList.isEmpty()) {
            menuList = storeMenuService.getUserRoleMenu(clerkId);
            cache.put(cacheKey, menuList);
        }
        return menuList;
    }


    @Override
    public void updateRoleMenu(String roleId, List<StoreMenuRole> roleMenus) {
        try {
            roleMenus.forEach(role -> role.setStoreId(UserContext.getCurrentUser().getStoreId()));
            //删除角色已经绑定的菜单
            this.delete(roleId);
            //重新保存角色菜单关系
            this.saveBatch(roleMenus);

            cache.vagueDel(CachePrefix.PERMISSION_LIST.getPrefix(UserEnums.STORE));
            cache.vagueDel(CachePrefix.STORE_USER_MENU.getPrefix());
        } catch (Exception e) {
            log.error("修改用户权限错误", e);
        }
    }

    @Override
    public void delete(String roleId) {
        //删除
        QueryWrapper<StoreMenuRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        this.remove(queryWrapper);
        cache.vagueDel(CachePrefix.PERMISSION_LIST.getPrefix(UserEnums.STORE));
        cache.vagueDel(CachePrefix.STORE_USER_MENU.getPrefix());
    }

    @Override
    public void delete(List<String> roleId) {
        //删除
        QueryWrapper<StoreMenuRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleId);
        this.remove(queryWrapper);
        cache.vagueDel(CachePrefix.PERMISSION_LIST.getPrefix(UserEnums.STORE));
        cache.vagueDel(CachePrefix.STORE_USER_MENU.getPrefix());
    }
}
