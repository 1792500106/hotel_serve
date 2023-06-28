package com.java.study.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.modules.sys.dao.SysLogDao;
import com.java.study.modules.sys.entity.SysLogEntity;
import com.java.study.modules.sys.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        Long createUserId = (Long) params.get("createUserId");
        IPage<SysLogEntity> page = this.page(
            new Query<SysLogEntity>().getPage(params),
            new QueryWrapper<SysLogEntity>()
                    .like(StringUtils.isNotBlank(key), "username", key)
                    .eq(createUserId != null, "user_id", createUserId)
        );

        return new PageUtils(page);
    }
}
