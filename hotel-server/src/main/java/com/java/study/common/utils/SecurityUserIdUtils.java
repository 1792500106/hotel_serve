package com.java.study.common.utils;

import com.java.study.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;

/**
 * @ClassDesc  获取登录用户ID
 */
public class SecurityUserIdUtils {

    public static SysUserEntity getUser() {
        return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }

}
