package com.java.study.modules.personal.service.vo;

import lombok.Data;

/**
 * @ClassDesc  用户评论房间信息
 */
@Data
public class UserCommentsHotel {

    /** 订单ID */
    private Long orderId;
    
    /** 内容 */
    private String content;
}
