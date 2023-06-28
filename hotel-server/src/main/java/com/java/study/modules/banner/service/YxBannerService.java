package com.java.study.modules.banner.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.study.common.utils.PageUtils;
import com.java.study.modules.banner.entity.YxBanner;

import java.util.Map;

/**
 * Service 层
 */
public interface YxBannerService extends IService<YxBanner> {

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
    void saveData(YxBanner resources);


    /**
     * 修改
     *
     * @param resources
     */
    void updateData(YxBanner resources);


    /**
     * 删除
     *
     * @param ids
     */
    void delData(Long[] ids);

}
