package com.java.study.modules.hotel.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.hotel.entity.YxComments;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface YxCommentsMapper extends BaseMapper<YxComments> {


}
