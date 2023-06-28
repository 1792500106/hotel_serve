package com.java.study.modules.order.service.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassDesc 管理员给用户延长开房时间
 */
@Data
public class AdminExtendVo {

    /**
     * 房间ID
     */
    private Long id;

    /**
     * 延长得时间
     */
    private String endTime;

    /**
     * 延长得时间
     */
    private Date endTimeFor;

}
