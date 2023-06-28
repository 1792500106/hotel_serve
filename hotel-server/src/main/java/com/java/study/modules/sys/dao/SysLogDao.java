package com.java.study.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统日志
 */
@Repository
@Mapper
public interface SysLogDao extends BaseMapper<SysLogEntity> {

}
