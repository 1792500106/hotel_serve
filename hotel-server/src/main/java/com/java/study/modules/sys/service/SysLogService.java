package com.java.study.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.study.common.utils.PageUtils;
import com.java.study.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 */
public interface SysLogService extends IService<SysLogEntity> {
    /**
     * 分页查询用户数据
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);


}
