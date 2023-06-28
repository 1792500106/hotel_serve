package com.java.study.modules.notice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.modules.notice.dao.YxNoticeMapper;
import com.java.study.modules.notice.entity.YxNotice;
import com.java.study.modules.notice.service.YxNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Service 实现类层
 */
@Service
public class YxNoticeServiceImpl extends ServiceImpl<YxNoticeMapper, YxNotice> implements YxNoticeService {

    /**
     * 查询文章列表时返回的字段（过滤掉详情字段以加快速度）
     */
    private static final String LIST_FILEDS = "*";

    @Autowired
    private YxNoticeMapper yxNoticeMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        IPage<YxNotice> page = this.page(
                new Query<YxNotice>().getPage(params),
                new QueryWrapper<YxNotice>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "title", title)
                        .eq("is_del", 0)
        );
        return new PageUtils(page);
    }

    /**
     * 保存
     */
    @Override
    public void saveData(YxNotice resources) {
        resources.setCreatedTime(new Date());
        resources.setIsDel(0);
        yxNoticeMapper.insert(resources);
    }

    /**
     * 修改
     */
    @Override
    public void updateData(YxNotice resources) {
        yxNoticeMapper.updateById(resources);
    }

    /**
     * 删除
     */
    @Override
    public void delData(Long[] ids) {
        for (Long id : ids) {
            YxNotice resources = new YxNotice();
            resources.setId(id);
            resources.setIsDel(1);
            yxNoticeMapper.updateById(resources);
        }
    }
}
