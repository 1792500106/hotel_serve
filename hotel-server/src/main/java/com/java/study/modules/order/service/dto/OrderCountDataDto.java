package com.java.study.modules.order.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassDesc 订单统计图表数据
 */
@Data
public class OrderCountDataDto {

    /**
     * 订单数量
     */
    private Integer dayNum;

    /**
     * 订单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8")
    private Date dayTime;

    /**
     * 订单金额
     */
    private BigDecimal dayMoney;
}
