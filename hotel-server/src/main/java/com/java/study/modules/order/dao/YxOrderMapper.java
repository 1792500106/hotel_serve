package com.java.study.modules.order.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.order.entity.YxOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface YxOrderMapper extends BaseMapper<YxOrder> {

    /**
     * 获取订单金额
     */
    @Select(" SELECT sum(price) FROM `yx_order` WHERE is_del=0 and created_time < #{eveningTime} and created_time > #{morningTime} ")
    Double getOrderDayMoney(@Param("morningTime") String morningTime, @Param("eveningTime") String eveningTime);

    /**
     * 统计房间分类的金额
     */
    @Select(" SELECT sum(price) FROM `yx_order` WHERE is_del=0 and `status`!=0 and `status`!=2 and hotel_type_id= #{hotelTypeId}")
    Double sumOrderType(@Param("hotelTypeId") Long hotelTypeId);

    /**
     * 订单总销量
     */
    @Select(" SELECT sum(price) FROM `yx_order` WHERE is_del=0 and `status`!=2 ")
    Double sumOrderMoneyPrice();
}
