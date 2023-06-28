package com.java.study.modules.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName(value = "yx_hotel")
public class YxHotel {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类ID
     */
    private Long typeId;

    /**
     * 房间号
     */
    private String homeNum;
    /**
     * 房间名称
     */
    private String homeName;
    /**
     * 今日价格
     */
    private BigDecimal newPrice;
    /**
     * 房间图片
     */
    private String photos;
    /**
     * 状态;0空房1已预定2脏房
     */
    private Integer status;
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
     * 订房数量
     */
    private Integer numberOf;



}
