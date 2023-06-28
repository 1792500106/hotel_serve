package com.java.study.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.sys.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 系统用户Token
 */
@Repository
@Mapper
@CacheNamespace(flushInterval = 300000L)//缓存五分钟过期
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

    SysUserTokenEntity queryByToken(String token);

}
