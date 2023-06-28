package com.java.study.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.sys.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户与角色对应关系
 */
@Repository
@Mapper
@CacheNamespace(flushInterval = 300000L)//缓存五分钟过期
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);


    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
