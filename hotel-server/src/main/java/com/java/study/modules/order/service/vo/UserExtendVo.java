package com.java.study.modules.order.service.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassDesc 用户自己延长开房时间
 */
@Data
public class UserExtendVo {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 延长得时间
     */
    private String endTime;

    /**
     * 延长得时间
     */
    private Date endTimeFor;

}
