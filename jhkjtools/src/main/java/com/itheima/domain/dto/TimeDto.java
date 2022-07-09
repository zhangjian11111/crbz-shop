package com.itheima.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author zhangjian
 * @email 1097951193@qq.com
 * @date 2022-05-18 22:27:09
 */
@Data
public class TimeDto implements Serializable {
private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    @ExcelIgnore
    private Integer id;
    /**
     * 序号
     */
    @ExcelProperty("序号")
    private String serial;
    /**
     * 事件描述
     */
    @ExcelProperty("事件描述")
    private String description;
    /**
     * 提出人
     */
    @ExcelProperty("提出人")
    private String proposer;
    /**
     * 解决人
     */
    @ExcelProperty("解决人")
    private String solver;
    /**
     * 预计解决时间
     */
    @ExcelProperty("预计解决时间")
    private String ertime;
    /**
     * 实际解决时间
     */
    @ExcelProperty("实际解决时间")
    private String artime;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;
    /**
     * 解决状态
     */
    @ExcelProperty("解决状态")
    private String rstatus;

    /**
     * 开始时间
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private String starttime;

    /**
     * 结束时间
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endtime;


}
