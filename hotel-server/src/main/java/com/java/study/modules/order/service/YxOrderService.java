package com.java.study.modules.order.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.study.common.utils.PageUtils;
import com.java.study.modules.order.entity.YxOrder;
import com.java.study.modules.order.service.dto.GetOrderCountListDto;
import com.java.study.modules.order.service.vo.AdminExtendVo;
import com.java.study.modules.order.service.vo.UserExtendVo;
import com.java.study.modules.personal.service.vo.UserRechargeVo;

import java.util.Map;

/**
 * Service 层
 */
public interface YxOrderService extends IService<YxOrder> {

    /**
     * 分页查询用户数据
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    PageUtils queryPage(Map<String, Object> params);


    /**
     * 退房
     *
     * @param ids
     */
    void check(Long[] ids);

    /**
     * 管理员延长开房时间
     */
    void adminOrderExtend(AdminExtendVo resources);


    /**
     * 用户充值
     */
    String userRecharge(UserRechargeVo resources);

    /**
     * 用户查询个人订单
     */
    PageUtils userOrderList(Map<String, Object> params);

    /**
     * 用户查询当前用户得房间
     */
    PageUtils userHome(Map<String, Object> params);


    /**
     * 删除
     *
     * @param ids
     */
    void delData(Long[] ids);


    /**
     * 用户自己延长开房时间
     */
    void userOrderExtend(UserExtendVo resources);

    /**
     * 订单统计
     */
    GetOrderCountListDto orderCount();

    /**
     * 用户个人退房
     */
    void userOrderQuit(Long ids);

    /**
     * 管理员给用户退房
     */
    void adminOrderQuit(Long ids);

    /**
     * 管理员给用户办理入住的手续
     */
    void adminOrderDetermine(Long ids);
}
