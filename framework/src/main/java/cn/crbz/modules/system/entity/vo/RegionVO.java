package cn.crbz.modules.system.entity.vo;

import cn.crbz.common.utils.BeanUtil;
import cn.crbz.modules.system.entity.dos.Region;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区VO
 *
 * @author Chopper
 * @since 2021-02-08 09:49
 */
@Data
@NoArgsConstructor
public class RegionVO extends Region {

    /**
     * 子信息
     */
    private List<RegionVO> children;

    public RegionVO(Region region) {
        BeanUtil.copyProperties(region, this);
        this.children = new ArrayList<>();
    }
}
