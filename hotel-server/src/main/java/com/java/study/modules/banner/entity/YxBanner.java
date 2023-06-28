package com.java.study.modules.banner.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName(value = "yx_banner")
public class YxBanner {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;
    /**
     * 图片
     */
    private String photos;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8")
    private Date createdTime;
    /**
     * 是否删除;0否1是
     */
    private Integer isDel;
}
