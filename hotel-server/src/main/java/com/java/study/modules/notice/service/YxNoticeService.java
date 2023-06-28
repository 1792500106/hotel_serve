package com.java.study.modules.notice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.study.common.utils.PageUtils;
import com.java.study.modules.notice.entity.YxNotice;

import java.util.Map;

/**
 * Service 层
 */
public interface YxNoticeService extends IService<YxNotice> {

    /**
     * 分页查询用户数据
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存
     *
     * @param resources
     */
    void saveData(YxNotice resources);


    /**
     * 修改
     *
     * @param resources
     */
    void updateData(YxNotice resources);


    /**
     * 删除
     *
     * @param ids
     */
    void delData(Long[] ids);

}
