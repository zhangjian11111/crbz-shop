package cn.crbz.modules.member.mapper;

import cn.crbz.modules.member.entity.dos.FootPrint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * 浏览历史数据处理层
 *
 * @author Chopper
 * @since 2020-02-25 14:10:16
 */
public interface FootprintMapper extends BaseMapper<FootPrint> {
    /**
     * 删除超过100条后的记录
     *
     * @param memberId 会员ID
     */
    @Delete("DELETE crbz_foot_print " +
            "FROM crbz_foot_print " +
            "LEFT JOIN ( " +
            "  SELECT id " +
            "  FROM ( " +
            "    SELECT id " +
            "    FROM crbz_foot_print " +
            "    WHERE member_id = ${memberId} " +
            "    ORDER BY create_time DESC " +
            "    LIMIT 100 " +
            "  ) AS keep " +
            ") AS latest_footprints " +
            "ON crbz_foot_print.id = latest_footprints.id " +
            "WHERE crbz_foot_print.member_id = ${memberId} AND latest_footprints.id IS NULL; ")
    void deleteLastFootPrint(String memberId);

}
