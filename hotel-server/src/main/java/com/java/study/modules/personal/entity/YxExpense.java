package com.java.study.modules.personal.entity;

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
@TableName(value = "yx_expense")
public class YxExpense {

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
     * 标题
     */
    private String title;
    /**
     * 类型
     */
    private String status;
    /**
     * 具体金额
     */
    private BigDecimal price;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdTime;
    /**
     * 是否删除;0否1是
     */
    private Integer isDel;

}
