package com.java.study.modules.personal.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.study.common.utils.PageUtils;
import com.java.study.modules.personal.entity.YxExpense;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Service 层
 */
public interface YxExpenseService extends IService<YxExpense> {

    /**
     * 分页查询用户数据
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除
     *
     * @param ids
     */
    void delData(Long[] ids);


    /**
     * 添加用户流水
     * @return
     */
    String saveUserExpense(Long userId, String userName, String title, String status, BigDecimal price);


}
