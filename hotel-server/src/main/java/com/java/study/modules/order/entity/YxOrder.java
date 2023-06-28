package com.java.study.modules.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassDesc
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName(value = "yx_order")
public class YxOrder {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 房间ID
     */
    private Long homeId;

    /**
     * 房间号
     */
    private String homeNum;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 入住时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    /**
     * 离开时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
    /**
     * 天数
     */
    private Integer startDay;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 状态 0未付款1已付款未入住2已退款3已完结4已付款已入住
     */
    private Integer status;
    /**
     * 是否延长;0否1是
     */
    private Integer isExtended;
    /**
     * 延长时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date extendedTime;
    /**
     * 实际退回金额
     */
    private BigDecimal refundPrice;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdTime;
    /**
     * 是否删除;0否1是
     */
    private Integer isDel;

    /**
     * 用户身份证
     */
    private String userCard;

    /**
     * 房间名称
     */
    @TableField(exist = false)
    private String homeName;

    /**
     * 行程
     */
    private String trips;
    /**
     * 房间分类ID
     */
    private Long hotelTypeId;
}
