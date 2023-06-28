package com.java.study.modules.hotel.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.study.common.utils.PageUtils;
import com.java.study.modules.hotel.entity.YxComments;
import com.java.study.modules.personal.service.vo.UserCommentsHotel;

import java.util.Map;

/**
 * Service 层
 */
public interface YxCommentsService extends IService<YxComments> {

    /**
     * 分页查询用户数据  管理员
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryAdminPage(Map<String, Object> params);


    /**
     * 分页查询用户数据  用户
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryUserPage(Map<String, Object> params);


    /**
     * 保存
     *
     * @param resources
     */
    void saveData(YxComments resources);


    /**
     * 修改
     *
     * @param resources
     */
    void updateData(YxComments resources);


    /**
     * 删除
     *
     * @param ids
     */
    void delData(Long[] ids);

    /**
     * 用户评论房间信息
     */
    void userCommentsHotel(UserCommentsHotel resources);


}
