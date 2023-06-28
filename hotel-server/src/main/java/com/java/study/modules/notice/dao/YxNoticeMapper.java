package com.java.study.modules.notice.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.notice.entity.YxNotice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface YxNoticeMapper extends BaseMapper<YxNotice> {
}
