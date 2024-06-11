package cn.crbz.modules.member.entity.vo;

import cn.crbz.common.utils.BeanUtil;
import cn.crbz.modules.member.entity.dos.StoreDepartment;
import cn.crbz.modules.permission.entity.dos.Department;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门VO
 *
 * @author Chopper
 * @since 2020-11-23 18:48
 */
@Data
public class StoreDepartmentVO extends StoreDepartment {

    private List<StoreDepartmentVO> children = new ArrayList<>();

    public StoreDepartmentVO() {
    }

    public StoreDepartmentVO(StoreDepartment storeDepartment) {
        BeanUtil.copyProperties(storeDepartment, this);
    }
}
