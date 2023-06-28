package com.java.study.modules.hotel.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.study.common.utils.PageUtils;
import com.java.study.modules.hotel.entity.YxHotelType;

import java.util.Map;

/**
 * Service 层
 */
public interface YxHotelTypeService extends IService<YxHotelType> {

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
    void saveData(YxHotelType resources);


    /**
     * 修改
     *
     * @param resources
     */
    void updateData(YxHotelType resources);


    /**
     * 删除
     *
     * @param ids
     */
    void delData(Long[] ids);

}
