package com.java.study.modules.order.service.dto;

import com.java.study.modules.hotel.entity.YxHotelType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassDesc 订单统计返回数据
 */
@Data
public class GetOrderCountListDto {

    /**
     * 今日订单总数
     */
    private Integer todayOrderNum;
    /**
     * 今日订单总额
     */
    private BigDecimal todayOrderMoney;

    /**
     * 昨天订单总数
     */
    private Integer yesterdayOrderNum;
    /**
     * 昨天订单总额
     */
    private BigDecimal yesterdayOrderMoney;

    /**
     * 图表数据
     */
    private List<OrderCountDataDto> orderCountList;

    /**
     * 分类销售统计
     */
    private List<YxHotelType> yxHotelTypeList;

    /**
     * 总订单数
     */
    private Integer sumOrderNum;
    /**
     * 总销量
     */
    private BigDecimal sumOrderMoney;
}
