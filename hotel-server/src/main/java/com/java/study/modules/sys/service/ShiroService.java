package com.java.study.modules.sys.service;

import com.java.study.modules.sys.entity.SysUserEntity;
import com.java.study.modules.sys.entity.SysUserTokenEntity;

import java.util.Set;

/**
 * shiro相关接口
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    SysUserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId 用户ID
     * @return SysUserEntity 管理用户
     */
    SysUserEntity queryUser(Long userId);
}
