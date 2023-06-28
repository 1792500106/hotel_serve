package com.java.study.modules.hotel.service.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassDesc
 */
@Data
public class ShowHotelMoneyVo {

    /**
     * 订单之后的金额
     */
    private BigDecimal hotelMoney;
}
