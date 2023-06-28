package com.java.study.modules.hotel.service.vo;

import com.java.study.common.utils.PageUtils;
import com.java.study.modules.hotel.entity.YxHotel;
import lombok.Data;

/**
 * @ClassDesc  显示房间详情
 */
@Data
public class HotelDetailVo {

    /** 房间信息 */
    private YxHotel yxHotel;
    
    /** 房间评论 */
    private PageUtils pageUtils;
}
