package com.itheima.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 *
 *
 * @author zhangjian
 * @email 1097951193@qq.com
 * @date 2022-05-18 22:27:09
 */
@Data
@TableName("zbforcoshine")
/*public class ZbforcoshineEntity implements Serializable {
	private static final long serialVersionUID = 1L;*/

public class ZbforcoshineEntity implements Serializable {
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
	 * 提出时间
	 */
	@ExcelProperty("提出时间")
//	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private String proposetime;
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

}
