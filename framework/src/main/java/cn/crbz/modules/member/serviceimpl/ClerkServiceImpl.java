package cn.crbz.modules.member.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.crbz.cache.Cache;
import cn.crbz.cache.CachePrefix;
import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.common.security.context.UserContext;
import cn.crbz.common.security.enums.UserEnums;
import cn.crbz.common.utils.StringUtils;
import cn.crbz.common.vo.PageVO;
import cn.crbz.modules.member.entity.dos.Clerk;
import cn.crbz.modules.member.entity.dos.Member;
import cn.crbz.modules.member.entity.dos.StoreClerkRole;
import cn.crbz.modules.member.entity.dos.StoreRole;
import cn.crbz.modules.member.entity.dto.ClerkAddDTO;
import cn.crbz.modules.member.entity.dto.ClerkEditDTO;
import cn.crbz.modules.member.entity.dto.ClerkQueryDTO;
import cn.crbz.modules.member.entity.vo.ClerkVO;
import cn.crbz.modules.member.mapper.ClerkMapper;
import cn.crbz.modules.member.service.*;
import cn.crbz.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店员业务实现
 *
 * @author wget
 * @title: ClerkServiceImpl
 * @projectName crbzshop
 * @date 2021/12/28 8:13 下午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ClerkServiceImpl extends ServiceImpl<ClerkMapper, Clerk> implements ClerkService {

    @Autowired
    private StoreRoleService storeRoleService;
    @Autowired
    private StoreDepartmentService storeDepartmentService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private StoreClerkRoleService storeClerkRoleService;
    @Autowired
    private Cache cache;

    @Override
    public IPage<ClerkVO> clerkForPage(PageVO page, ClerkQueryDTO clerkQueryDTO) {

        QueryWrapper<ClerkVO> clerkVOQueryWrapper = new QueryWrapper<>();
        clerkVOQueryWrapper.eq("crbz_clerk.store_id", UserContext.getCurrentUser().getStoreId());
        clerkVOQueryWrapper.eq(StringUtils.isNotEmpty(clerkQueryDTO.getDepartmentId()), "crbz_clerk.department_id", clerkQueryDTO.getDepartmentId());
        clerkVOQueryWrapper.like(StringUtils.isNotEmpty(clerkQueryDTO.getClerkName()), "crbz_clerk.clerk_name", clerkQueryDTO.getClerkName());
        clerkVOQueryWrapper.like(StringUtils.isNotEmpty(clerkQueryDTO.getMobile()), "m.mobile", clerkQueryDTO.getMobile());

        return this.baseMapper.selectClerkPage(PageUtil.initPage(page), clerkVOQueryWrapper);


        /*Page<Clerk> clerkPage = page(initPage, initWrapper);

        if (clerkPage.getRecords().size() > 0) {
            List<StoreRole> roles = storeRoleService.list(new QueryWrapper<StoreRole>()
                    .eq("store_id", UserContext.getCurrentUser().getStoreId()));

            List<StoreDepartment> departments = storeDepartmentService.list(new QueryWrapper<StoreDepartment>()
                    .eq("store_id", UserContext.getCurrentUser().getStoreId()));

            List<String> memberIds = new ArrayList<>();
            clerkPage.getRecords().forEach(clerk -> {
                memberIds.add(clerk.getMemberId());
            });
            List<Member> members = memberService.list(new QueryWrapper<Member>().in("id", memberIds));

            List<ClerkVO> result = new ArrayList<>();

            clerkPage.getRecords().forEach(clerk -> {
                ClerkVO clerkVO = new ClerkVO(clerk);
                if (!CharSequenceUtil.isEmpty(clerk.getDepartmentId())) {
                    try {
                        clerkVO.setDepartmentTitle(
                                departments.stream().filter
                                        (department -> department.getId().equals(clerk.getDepartmentId()))
                                        .collect(Collectors.toList())
                                        .get(0)
                                        .getTitle()
                        );
                    } catch (Exception e) {
                        log.error("填充部门信息异常", e);
                    }
                }
                clerkVO.setMobile(
                        members.stream().filter
                                (member -> member.getId().equals(clerk.getMemberId()))
                                .collect(Collectors.toList())
                                .get(0)
                                .getMobile()
                );
                if (!StringUtils.isEmpty(clerk.getRoleIds())) {
                    try {
                        List<String> memberRoles = Arrays.asList(clerk.getRoleIds().split(","));
                        clerkVO.setRoles(
                                roles.stream().filter
                                        (role -> memberRoles.contains(role.getId()))
                                        .collect(Collectors.toList())
                        );
                    } catch (Exception e) {
                        log.error("填充部门信息异常", e);
                    }
                }
                result.add(clerkVO);
            });
            Page<ClerkVO> pageResult = new Page(clerkPage.getCurrent(), clerkPage.getSize(), clerkPage.getTotal());
            pageResult.setRecords(result);
            return pageResult;
        }
        return new Page<ClerkVO>();*/
    }


    @Override
    public ClerkVO get(String id) {
        Clerk clerk = this.getById(id);
        ClerkVO clerkVO = new ClerkVO(clerk);
        //手机号码
        clerkVO.setMobile(memberService.getById(clerk.getMemberId()).getMobile());
        if (!CharSequenceUtil.isEmpty(clerk.getDepartmentId())) {
            clerkVO.setDepartmentTitle(storeDepartmentService.getById(clerk.getDepartmentId()).getTitle());
        }
        if (!StringUtils.isEmpty(clerk.getRoleIds())) {
            List<String> memberRoles = Arrays.asList(clerk.getRoleIds().split(","));
            List<StoreRole> roles = storeRoleService.list(new QueryWrapper<StoreRole>()
                    .eq("store_id", UserContext.getCurrentUser().getStoreId()));
            clerkVO.setRoles(
                    roles.stream().filter
                                    (role -> memberRoles.contains(role.getId()))
                            .collect(Collectors.toList())
            );
        }
        return clerkVO;
    }

    @Override
    public Clerk updateClerk(ClerkEditDTO clerkEditDTO) {
        Clerk clerk = this.getById(clerkEditDTO.getId());
        if (clerk != null) {
            //编辑店主限制
            if (Boolean.TRUE.equals(clerk.getShopkeeper())) {
                throw new ServiceException(ResultCode.CANT_EDIT_CLERK_SHOPKEEPER);
            }

            //校验当前店员是否是当前店铺的
            if (!clerk.getStoreId().equals(UserContext.getCurrentUser().getStoreId())) {
                throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
            }
            if (Boolean.TRUE.equals(clerkEditDTO.getIsSuper())) {
                clerk.setRoleIds("");
            } else {
                //角色赋值
                if (!clerkEditDTO.getRoles().isEmpty()) {
                    clerk.setRoleIds(CharSequenceUtil.join(",", clerkEditDTO.getRoles()));
                    //添加店员用户角色
                    List<StoreClerkRole> storeClerkRoleList = new ArrayList<>();

                    clerkEditDTO.getRoles().forEach(a -> storeClerkRoleList.add(StoreClerkRole.builder().clerkId(clerk.getId()).roleId(a).build()));

                    storeClerkRoleService.saveBatch(storeClerkRoleList);
                } else {
                    clerk.setRoleIds("");
                    LambdaQueryWrapper<StoreClerkRole> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(StoreClerkRole::getClerkId, clerk.getId());
                    storeClerkRoleService.remove(queryWrapper);
                }
            }

            //部门校验
            if (CharSequenceUtil.isNotEmpty(clerkEditDTO.getDepartmentId())) {
                if (storeDepartmentService.getById(clerkEditDTO.getDepartmentId()) != null) {
                    clerk.setDepartmentId(clerkEditDTO.getDepartmentId());
                } else {
                    throw new ServiceException(ResultCode.PERMISSION_NOT_FOUND_ERROR);
                }
            } else {
                clerk.setDepartmentId("");
            }

            cache.vagueDel(CachePrefix.PERMISSION_LIST.getPrefix(UserEnums.STORE) + clerk.getMemberId());
            cache.vagueDel(CachePrefix.STORE_USER_MENU.getPrefix() + clerk.getMemberId());
            clerk.setIsSuper(clerkEditDTO.getIsSuper());
            this.updateById(clerk);
            return clerk;
        }
        throw new ServiceException(ResultCode.CLERK_NOT_FOUND_ERROR);
    }

    @Override
    public Clerk saveClerk(ClerkAddDTO clerkAddDTO) {
        Clerk clerk = new Clerk(clerkAddDTO);
        clerk.setShopkeeper(clerkAddDTO.getShopkeeper());
        clerk.setIsSuper(clerkAddDTO.getIsSuper());
        //校验此会员是否已经是店员
        Clerk temp = this.getClerkByMemberId(clerkAddDTO.getMemberId());

        //店员信息不为空
        if (temp != null && !temp.getStoreId().equals(clerkAddDTO.getStoreId())) {
            throw new ServiceException(ResultCode.CLERK_USER_ERROR);
        }
        if (temp != null) {
            throw new ServiceException(ResultCode.CLERK_ALREADY_EXIT_ERROR);
        }
        //部门校验
        if (CharSequenceUtil.isNotEmpty(clerkAddDTO.getDepartmentId()) && storeDepartmentService.getById(clerkAddDTO.getDepartmentId()) == null) {
            throw new ServiceException(ResultCode.PERMISSION_NOT_FOUND_ERROR);
        }
        //角色校验
        if (clerkAddDTO.getRoles() != null && !clerkAddDTO.getRoles().isEmpty()) {
            List<StoreRole> storeRoles = storeRoleService.list(clerkAddDTO.getRoles());
            if (storeRoles.size() != clerkAddDTO.getRoles().size()) {
                throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
            }
        }

        this.save(clerk);

        //判断用户角色权限不为超级会员且权限路径不为空
        if (Boolean.FALSE.equals(clerkAddDTO.getIsSuper()) && clerkAddDTO.getRoles() != null) {
            //添加店员用户角色
            List<StoreClerkRole> storeClerkRoleList = new ArrayList<>();

            clerkAddDTO.getRoles().forEach(a -> storeClerkRoleList.add(StoreClerkRole.builder().clerkId(clerk.getId()).roleId(a).build()));

            storeClerkRoleService.saveBatch(storeClerkRoleList);
        }

        return clerk;
    }

    @Override
    public Clerk getClerkByMemberId(String memberId) {
        return this.getOne(new QueryWrapper<Clerk>().eq("member_id", memberId));
    }

    @Override
    public void resetPassword(List<String> ids) {
        QueryWrapper<Clerk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", UserContext.getCurrentUser().getStoreId());
        queryWrapper.in("id", ids);
        List<Clerk> clerks = this.baseMapper.selectList(queryWrapper);
        //校验要重置的店员是否是当前店铺的店员
        if (clerks.size() != ids.size()) {
            throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
        }
        //店员密码就是会员密码所以要组织会员修改密码参数信息
        List<String> memberIds = new ArrayList<>();
        clerks.forEach(clerk -> {
            //如果是店主无法重置密码
            if (Boolean.TRUE.equals(clerk.getShopkeeper())) {
                throw new ServiceException(ResultCode.CLERK_SUPPER);
            }
            memberIds.add(clerk.getMemberId());
        });
        memberService.resetPassword(memberIds);
    }


    @Override
    public void deleteClerk(List<String> ids) {
        QueryWrapper<Clerk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", UserContext.getCurrentUser().getStoreId());
        queryWrapper.in("id", ids);
        List<Clerk> clerks = this.baseMapper.selectList(queryWrapper);
        if (!clerks.isEmpty()) {
            //校验要重置的店员是否是当前店铺的店员
            if (clerks.size() != ids.size()) {
                throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
            }
            //删除店员
            this.removeByIds(ids);
            //更改会员为为拥有店铺
            List<String> memberIds = new ArrayList<>();
            clerks.forEach(clerk -> {
                //无法删除当前登录的店员
                if (UserContext.getCurrentUser().getClerkId().equals(clerk.getId())) {
                    throw new ServiceException(ResultCode.CLERK_CURRENT_SUPPER);
                }
                //无法删除店主
                if (Boolean.TRUE.equals(clerk.getShopkeeper())) {
                    throw new ServiceException(ResultCode.CLERK_SUPPER);
                }
                memberIds.add(clerk.getMemberId());
            });
            memberService.updateHaveShop(false, null, memberIds);
        }
    }

    @Override
    public Member checkClerk(String mobile) {
        //校验是否已经是会员
        Member member = memberService.findByMobile(mobile);
        if (member != null) {
            //校验要添加的会员是否已经是店主
            if (Boolean.TRUE.equals(member.getHaveStore())) {
                throw new ServiceException(ResultCode.STORE_APPLY_DOUBLE_ERROR);
            }
            //校验会员的有效性
            if (Boolean.FALSE.equals(member.getDisabled())) {
                throw new ServiceException(ResultCode.USER_STATUS_ERROR);
            }
            //校验此会员是否已经是店员
            Clerk clerk = this.getClerkByMemberId(member.getId());
            if (clerk != null && !clerk.getStoreId().equals(UserContext.getCurrentUser().getStoreId())) {
                throw new ServiceException(ResultCode.CLERK_USER_ERROR);
            }
            if (clerk != null && clerk.getStoreId().equals(UserContext.getCurrentUser().getStoreId())) {
                throw new ServiceException(ResultCode.CLERK_ALREADY_EXIT_ERROR);
            }
            return member;
        }
        return new Member();
    }

    @Override
    public void disable(String id, Boolean status) {
        Clerk clerk = this.getById(id);
        if (clerk == null) {
            throw new ServiceException(ResultCode.USER_NOT_EXIST);
        }
        //店主无法禁用
        if (clerk.getShopkeeper() && clerk.getStatus()) {
            throw new ServiceException(ResultCode.CLERK_SUPPER);
        }
        clerk.setStatus(status);
        this.updateById(clerk);
    }
}
