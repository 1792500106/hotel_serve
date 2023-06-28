package com.java.study.modules.hotel.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.study.common.utils.PageUtils;
import com.java.study.modules.hotel.entity.YxHotel;
import com.java.study.modules.hotel.service.vo.ShowHotelMoneyVo;
import com.java.study.modules.hotel.service.vo.UserReserveVo;

import java.util.Map;

/**
 * Service 层
 */
public interface YxHotelService extends IService<YxHotel> {

    /**
     * 总酒店查询
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPageList(Map<String, Object> params);

    /**
     * 酒店预定的时候查询
     */
    PageUtils queryPageReserve(Map<String, Object> params);

    /**
     * 保存
     *
     * @param resources
     */
    void saveData(YxHotel resources);


    /**
     * 修改
     *
     * @param resources
     */
    void updateData(YxHotel resources);


    /**
     * 删除
     *
     * @param ids
     */
    void delData(Long[] ids);

    /**
     * 用户订房
     */
    void userReserve(UserReserveVo resources);

    /**
     * 管理员订房
     */
    void adminReserve(UserReserveVo resources);

    /**
     * 订房计算金额
     */
    ShowHotelMoneyVo hotelMoney(UserReserveVo resources);

}
