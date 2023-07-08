package cn.crbz.modules.permission.mapper;

import cn.crbz.modules.permission.entity.dos.Menu;
import cn.crbz.modules.permission.entity.vo.UserMenuVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单数据处理层
 *
 * @author Chopper
 * @since 2020-11-22 09:17
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户获取菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Select("SELECT menu.* FROM crbz_menu AS menu WHERE menu.id IN (" +
            "SELECT rm.menu_id FROM crbz_role_menu AS rm WHERE rm.role_id IN (" +
            "SELECT ur.role_id FROM crbz_user_role AS ur WHERE ur.user_id=#{userId}) OR rm.role_id IN (" +
            "SELECT dr.role_id FROM crbz_department_role AS dr WHERE dr.department_id =(" +
            "SELECT department_id FROM crbz_admin_user AS au WHERE au.id = #{userId})))")
    List<Menu> findByUserId(String userId);

    /**
     * 根据用户获取菜单权限
     *
     * @param userId 用户ID
     * @return 用户菜单VO列表
     */
    @Select("SELECT rm.is_super as is_super,m.*FROM crbz_menu AS m INNER JOIN crbz_role_menu AS rm ON rm.menu_id=m.id WHERE rm.role_id IN (" +
            "SELECT ur.role_id FROM crbz_user_role AS ur WHERE ur.user_id=#{userId}) OR rm.role_id IN (" +
            "SELECT dr.role_id FROM crbz_department_role AS dr INNER JOIN crbz_admin_user AS au ON au.department_id=dr.department_id " +
            "WHERE au.id=#{userId}) GROUP BY m.id,rm.is_super ORDER BY rm.is_super desc")
    List<UserMenuVO> getUserRoleMenu(String userId);
}
