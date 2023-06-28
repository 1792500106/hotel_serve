package com.java.study.modules.banner.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.modules.banner.dao.YxBannerMapper;
import com.java.study.modules.banner.entity.YxBanner;
import com.java.study.modules.banner.service.YxBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Service 实现类层
 */
@Service
public class YxBannerServiceImpl extends ServiceImpl<YxBannerMapper, YxBanner> implements YxBannerService {

    /**
     * 查询文章列表时返回的字段（过滤掉详情字段以加快速度）
     */
    private static final String LIST_FILEDS = "*";

    @Autowired
    private YxBannerMapper yxBannerMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        IPage<YxBanner> page = this.page(
                new Query<YxBanner>().getPage(params),
                new QueryWrapper<YxBanner>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "title", title)
                        .eq("is_del", 0)
                        .orderByDesc("sort")
        );
        return new PageUtils(page);
    }

    /**
     * 保存
     */
    @Override
    public void saveData(YxBanner resources) {
        resources.setCreatedTime(new Date());
        resources.setIsDel(0);
        yxBannerMapper.insert(resources);
    }

    /**
     * 修改
     */
    @Override
    public void updateData(YxBanner resources) {
        yxBannerMapper.updateById(resources);
    }

    /**
     * 删除
     */
    @Override
    public void delData(Long[] ids) {
        for (Long id : ids) {
            YxBanner resources = new YxBanner();
            resources.setId(id);
            resources.setIsDel(1);
            yxBannerMapper.updateById(resources);
        }
    }
}
