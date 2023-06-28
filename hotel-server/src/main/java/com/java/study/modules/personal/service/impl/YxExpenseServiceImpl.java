package com.java.study.modules.personal.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.modules.personal.dao.YxExpenseMapper;
import com.java.study.modules.personal.entity.YxExpense;
import com.java.study.modules.personal.service.YxExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Service 实现类层
 */
@Service
public class YxExpenseServiceImpl extends ServiceImpl<YxExpenseMapper, YxExpense> implements YxExpenseService {

    /**
     * 查询文章列表时返回的字段（过滤掉详情字段以加快速度）
     */
    private static final String LIST_FILEDS = "*";

    @Autowired
    private YxExpenseMapper yxExpenseMapper;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        Long createUserId = (Long) params.get("createUserId");
        IPage<YxExpense> page = this.page(
                new Query<YxExpense>().getPage(params),
                new QueryWrapper<YxExpense>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "title", title)
                        .eq(createUserId != null, "user_id", createUserId)
                        .eq("is_del", 0)
        );
        return new PageUtils(page);
    }

    /**
     * 删除
     */
    @Override
    public void delData(Long[] ids) {
        for (Long id : ids) {
            YxExpense resources = new YxExpense();
            resources.setId(id);
            resources.setIsDel(1);
            yxExpenseMapper.updateById(resources);
        }
    }

    @Override
    public String saveUserExpense(Long userId, String userName, String title, String status, BigDecimal price) {
        YxExpense resources = new YxExpense();
        resources.setUserId(userId);
        resources.setUserName(userName);
        resources.setTitle(title);
        resources.setStatus(status);
        resources.setPrice(price);
        resources.setCreatedTime(new Date());
        resources.setIsDel(0);
        yxExpenseMapper.insert(resources);
        return String.valueOf(resources.getId());
    }


}
