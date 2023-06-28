package com.java.study.modules.banner.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.banner.entity.YxBanner;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface YxBannerMapper extends BaseMapper<YxBanner> {
}
