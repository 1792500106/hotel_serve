package com.java.study.modules.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassDesc
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName(value = "yx_hotel_type")
public class YxHotelType {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String title;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8")
    private Date createdTime;
    /**
     * 是否删除;0否1是
     */
    private Integer isDel;

    /**
     * 房间分类的统计金额
     */
    @TableField(exist = false)
    private Double hotelTypeMoney;

}
