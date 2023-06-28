package com.java.study.modules.hotel.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassDesc 用户预定提交
 */
@Data
public class UserReserveVo {

    /**
     * 房间ID
     */
    private Long id;

    /**
     * 时间组
     */
    private String startTimes[];

    /**
     * 入住时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8")
    private Date startTime;
    /**
     * 离开时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8")
    private Date endTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户身份证
     */
    private String userCard;

    /**
     * 行程
     */
    private String trips;
}
