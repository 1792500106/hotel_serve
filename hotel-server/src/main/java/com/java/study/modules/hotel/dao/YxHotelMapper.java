package com.java.study.modules.hotel.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.hotel.entity.YxHotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface YxHotelMapper extends BaseMapper<YxHotel> {

    /**
     * 查询分类下面  有几个房间
     */
    @Select(" SELECT count(*) FROM yx_hotel WHERE is_del=0 AND type_id= #{typeId} ")
    int countHotelByTypeId(Long typeId);

    /**
     * 统计在售房间
     */
    @Select(" SELECT count(*) FROM `yx_hotel` WHERE is_del=0 AND status=0 ")
    Integer countHotel();
}
