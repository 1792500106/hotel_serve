package com.java.study.modules.order.service.impl;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.common.exception.RRException;
import com.java.study.common.utils.DateUtils;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.common.utils.SecurityUserIdUtils;
import com.java.study.config.AlipayConfig;
import com.java.study.modules.hotel.dao.YxHotelMapper;
import com.java.study.modules.hotel.dao.YxHotelTypeMapper;
import com.java.study.modules.hotel.entity.YxHotel;
import com.java.study.modules.hotel.entity.YxHotelType;
import com.java.study.modules.order.dao.YxOrderMapper;
import com.java.study.modules.order.entity.YxOrder;
import com.java.study.modules.order.service.YxOrderService;
import com.java.study.modules.order.service.dto.GetOrderCountListDto;
import com.java.study.modules.order.service.dto.OrderCountDataDto;
import com.java.study.modules.order.service.vo.AdminExtendVo;
import com.java.study.modules.order.service.vo.UserExtendVo;
import com.java.study.modules.personal.dao.SysUserMapper;
import com.java.study.modules.personal.entity.SysUser;
import com.java.study.modules.personal.service.YxExpenseService;
import com.java.study.modules.personal.service.vo.UserRechargeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service 实现类层
 */
@Service
public class YxOrderServiceImpl extends ServiceImpl<YxOrderMapper, YxOrder> implements YxOrderService {


    @Resource
    private AlipayConfig alipayConfig;

    /**
     * 查询文章列表时返回的字段（过滤掉详情字段以加快速度）
     */
    private static final String LIST_FILEDS = "*";

    @Autowired
    private YxOrderMapper yxOrderMapper;
    @Autowired
    private YxHotelMapper yxHotelMapper;

    @Autowired
    private YxExpenseService yxExpenseService;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private YxHotelTypeMapper yxHotelTypeMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        String status = (String) params.get("status");
        String toOrderId = (String) params.get("toOrderId");
        String typeId = (String) params.get("typeId");
        IPage<YxOrder> page = this.page(
                new Query<YxOrder>().getPage(params),
                new QueryWrapper<YxOrder>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "home_num", title)
                        .eq("is_del", 0)
                        .eq(StringUtils.hasText(status), "status", status)
                        .eq(StringUtils.hasText(toOrderId), "order_id", toOrderId)
                        .eq(StringUtils.hasText(typeId), "hotel_type_id", typeId)
        );
        return new PageUtils(page);
    }


    /**
     * 删除
     */
    @Override
    public void check(Long[] ids) {
        for (Long id : ids) {

            YxOrder resources = yxOrderMapper.selectById(id);
            if (resources.getStatus() == 3) {
                throw new RRException("该房间已经退房！");
            }

            /** 修改订单状态为完结 */
            resources.setStatus(3);
            yxOrderMapper.updateById(resources);

            /** 修改房间状态 */
            YxHotel yxHotel = yxHotelMapper.selectById(resources.getHomeId());
            yxHotel.setStatus(0);
            yxHotelMapper.updateById(yxHotel);
        }
    }


    /**
     * 管理员延长开房时间
     */
    @Override
    public void adminOrderExtend(AdminExtendVo resources) {

        /**
         * 管理员延长开房时间
         * 1.查询到该订单
         * 修改延长得时间
         * 修改金额
         */

        YxOrder yxOrder = yxOrderMapper.selectById(resources.getId());

        int days = DateUtils.differentDaysByMillisecond(yxOrder.getEndTime(), resources.getEndTimeFor());

        YxHotel yxHotel = yxHotelMapper.selectById(yxOrder.getHomeId());

        /** 计算钱 */

        BigDecimal newPrice = yxHotel.getNewPrice();
        BigDecimal daysBigDecimal = new BigDecimal(days);

        /** 最终要给钱 */
        BigDecimal userShiJiFangMoney = newPrice.multiply(daysBigDecimal);

        yxOrder.setPrice(yxOrder.getPrice().add(userShiJiFangMoney));
        yxOrder.setEndTime(resources.getEndTimeFor());
        yxOrder.setIsExtended(1);
        yxOrder.setExtendedTime(resources.getEndTimeFor());

        yxOrderMapper.updateById(yxOrder);


    }


    /**
     * 用户充值
     */
    @Override
    public String userRecharge(UserRechargeVo resources) {
        SysUser sysUser = sysUserMapper.selectById(SecurityUserIdUtils.getUserId());

        BigDecimal bigDecimal = new BigDecimal(resources.getMoney());

        /** 添加流水 */
        String id =  yxExpenseService.saveUserExpense(sysUser.getUserId(), sysUser.getNickName(), "用户充值", "充值", bigDecimal);

        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getURL(), alipayConfig.APPID, alipayConfig.RSA_PRIVATE_KEY , "json", alipayConfig.CHARSET, alipayConfig.ALIPAY_PUBLIC_KEY, alipayConfig.SIGNTYPE);
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = id;
        //付款金额，必填
        String total_amount = String.valueOf(bigDecimal);
        //订单名称，必填
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
            + "\"total_amount\":\""+ total_amount +"\","
            + "\"subject\":\""+ out_trade_no +"\","
            + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        alipayRequest.setReturnUrl("http://localhost:8080/");
        alipayRequest.setNotifyUrl("http://3mcid9.natappfree.cc/wx/yx_order/paysuccess");
        String body= null;
        try {
            body = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return body;
    }
    /**
     * 用户查询个人订单
     */
    @Override
    public PageUtils userOrderList(Map<String, Object> params) {
        String title = (String) params.get("title");
        Long createUserId = (Long) params.get("createUserId");
        IPage<YxOrder> page = this.page(
                new Query<YxOrder>().getPage(params),
                new QueryWrapper<YxOrder>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "home_num", title)
                        .eq("is_del", 0)
                        .eq(createUserId != null, "user_id", createUserId)
                        .orderByDesc("created_time")
        );
        return new PageUtils(page);
    }

    /**
     * 用户查询当前用户得房间
     */
    @Override
    public PageUtils userHome(Map<String, Object> params) {
        String title = (String) params.get("title");
        Long createUserId = (Long) params.get("createUserId");
        IPage<YxOrder> page = this.page(
                new Query<YxOrder>().getPage(params),
                new QueryWrapper<YxOrder>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "home_num", title)
                        .eq("is_del", 0)
                        .eq("status", 1)
                        .eq(createUserId != null, "user_id", createUserId)
                        .orderByDesc("created_time")
        );

        List<YxOrder> records = page.getRecords();
        for (YxOrder record : records) {

            /** 查询任务 */
            YxHotel yxHotel = yxHotelMapper.selectById(record.getHomeId());
            record.setHomeName(yxHotel.getHomeName() + "，房间号：" + yxHotel.getHomeNum());
        }
        page.setRecords(records);
        return new PageUtils(page);
    }

    /**
     * 删除
     */
    @Override
    public void delData(Long[] ids) {
        for (Long id : ids) {
            YxOrder resources = new YxOrder();
            resources.setId(id);
            resources.setIsDel(1);
            yxOrderMapper.updateById(resources);
        }
    }

    /**
     * 用户自己延长开房时间
     */
    @Override
    public void userOrderExtend(UserExtendVo resources) {
        /**
         * 用户自己延长开房时间
         * 1.查询到该订单
         * 2.判断余额够不够
         * 3.修改延长得时间
         * 4.修改金额
         * 5.插入流程
         * 6.修改用户余额
         */

        YxOrder yxOrder = yxOrderMapper.selectById(resources.getOrderId());

        int days = DateUtils.differentDaysByMillisecond(yxOrder.getEndTime(), resources.getEndTimeFor());

        YxHotel yxHotel = yxHotelMapper.selectById(yxOrder.getHomeId());

        /** 计算钱 */
        BigDecimal newPrice = yxHotel.getNewPrice();
        BigDecimal daysBigDecimal = new BigDecimal(days);

        /** 最终要给钱 */
        BigDecimal userShiJiFangMoney = newPrice.multiply(daysBigDecimal);

        /** 判断用户钱是否够用 */
        SysUser sysUser = sysUserMapper.selectById(SecurityUserIdUtils.getUserId());
        /** 用户的钱  减去房间的钱 */
        BigDecimal userShengYuMoney = sysUser.getMoney().subtract(userShiJiFangMoney);
        BigDecimal subtractFor = new BigDecimal(0);
        /** 判断金钱是否够用 */
        if (userShengYuMoney.compareTo(subtractFor) > 0) {
            yxOrder.setPrice(yxOrder.getPrice().add(userShiJiFangMoney));
            yxOrder.setEndTime(resources.getEndTimeFor());
            yxOrder.setIsExtended(1);
            yxOrder.setExtendedTime(resources.getEndTimeFor());

            yxOrderMapper.updateById(yxOrder);

            /** 插入流水 */
            yxExpenseService.saveUserExpense(sysUser.getUserId(), sysUser.getNickName(), "延长订购房间：" + yxHotel.getHomeName() + ",一共：" + days + "天", "消费", userShiJiFangMoney);

            /** 修改房间状态 */
            yxHotel.setStatus(1);
            yxHotelMapper.updateById(yxHotel);

            /** 修改用户余额 */
            sysUser.setMoney(userShengYuMoney);
            sysUserMapper.updateById(sysUser);

        } else {
            throw new RRException("余额不足！");
        }


    }

    /**
     * 订单统计
     */
    @Override
    public GetOrderCountListDto orderCount() {
        GetOrderCountListDto getOrderCountListDto = new GetOrderCountListDto();

        Integer sumOrderNum = yxOrderMapper.selectCount(new QueryWrapper<YxOrder>().eq("is_del", 0).ne("status", 2));
        Double aDouble1 = yxOrderMapper.sumOrderMoneyPrice();
        getOrderCountListDto.setSumOrderNum(sumOrderNum);
        getOrderCountListDto.setSumOrderMoney(new BigDecimal(aDouble1));

        List<OrderCountDataDto> orderCountDataDtoList = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            Date nowTimeFor = new Date();
            OrderCountDataDto orderCountDataDto = new OrderCountDataDto();

            Date nowDay = DateUtils.addDateDays(nowTimeFor, -i);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            String format = formatter.format(nowDay);

            /** 早晨时间 */
            String morningTime = format + " 00:00:00";
            /** 晚上时间 */
            String eveningTime = format + " 24:00:00";

            /** 订单数量 */
            QueryWrapper<YxOrder> wrapper = new QueryWrapper<>();
            wrapper.lambda()
                    .eq(YxOrder::getIsDel, 0)
                    .ge(YxOrder::getCreatedTime, morningTime)
                    .le(YxOrder::getCreatedTime, eveningTime);

            Integer orderNum = yxOrderMapper.selectCount(wrapper);


            /** 订单金额 */
            Double orderDayMoney = yxOrderMapper.getOrderDayMoney(morningTime, eveningTime);
            BigDecimal orderMoney;
            if (orderDayMoney == null) {
                orderMoney = new BigDecimal(0);
            } else {
                orderMoney = new BigDecimal(orderDayMoney);
            }


            orderCountDataDto.setDayNum(orderNum);
            orderCountDataDto.setDayMoney(orderMoney);
            orderCountDataDto.setDayTime(nowDay);

            if (i == 0) {
                getOrderCountListDto.setTodayOrderNum(orderNum);
                getOrderCountListDto.setTodayOrderMoney(orderMoney);
            }
            if (i == 1) {
                getOrderCountListDto.setYesterdayOrderNum(orderNum);
                getOrderCountListDto.setYesterdayOrderMoney(orderMoney);
            }
            orderCountDataDtoList.add(orderCountDataDto);

        }
        getOrderCountListDto.setOrderCountList(orderCountDataDtoList);

        List<YxHotelType> yxHotelTypeList = yxHotelTypeMapper.selectList(new QueryWrapper<YxHotelType>().eq("is_del", 0));

        for (YxHotelType yxHotelType : yxHotelTypeList) {
            Double aDouble = yxOrderMapper.sumOrderType(yxHotelType.getId());
            if (aDouble == null) {
                yxHotelType.setHotelTypeMoney(0D);
            } else {
                yxHotelType.setHotelTypeMoney(aDouble);
            }
        }

        getOrderCountListDto.setYxHotelTypeList(yxHotelTypeList);

        return getOrderCountListDto;
    }

    /**
     * 用户个人退房
     */
    @Override
    public void userOrderQuit(Long ids) {
        YxOrder yxOrder = yxOrderMapper.selectById(ids);
        if (yxOrder.getStatus() != 1) {
            throw new RRException("暂无权限！");
        }

        /**
         * 用户个人退房，是没有入住的情况下才可以退房
         * 1.修改房间的状态   为0  空房
         * 2.修改订单的状态  为2 已退款
         * 3.添加流水
         * 4.修改用户的金额
         * */

        YxHotel yxHotel = yxHotelMapper.selectById(yxOrder.getHomeId());
        yxHotel.setStatus(0);
        yxHotelMapper.updateById(yxHotel);

        yxOrder.setStatus(2);
        yxOrderMapper.updateById(yxOrder);

        /** 插入流水 */
        yxExpenseService.saveUserExpense(yxOrder.getUserId(), yxOrder.getUserName(), "个人退房：" + yxHotel.getHomeName(), "退房", yxOrder.getPrice());

        SysUser sysUser = sysUserMapper.selectById(yxOrder.getUserId());
        sysUser.setMoney(sysUser.getMoney().add(yxOrder.getPrice()));
        sysUserMapper.updateById(sysUser);


    }

    /**
     * 管理员给用户退房
     */
    @Override
    public void adminOrderQuit(Long ids) {

        YxOrder yxOrder = yxOrderMapper.selectById(ids);
        if (yxOrder.getStatus() != 1) {
            throw new RRException("该房间不能退房！");
        }

        /**
         * 管理员退房，是没有入住的情况下才可以退房
         * 1.修改房间的状态   为0  空房
         * 2.修改订单的状态  为2 已退款
         * 3.添加流水
         * 4.修改用户的金额
         * */

        YxHotel yxHotel = yxHotelMapper.selectById(yxOrder.getHomeId());
        yxHotel.setStatus(0);
        yxHotelMapper.updateById(yxHotel);

        yxOrder.setStatus(2);
        yxOrderMapper.updateById(yxOrder);

        /** 插入流水 */
        yxExpenseService.saveUserExpense(yxOrder.getUserId(), yxOrder.getUserName(), "管理员给用户退房：" + yxHotel.getHomeName(), "退房", yxOrder.getPrice());

        SysUser sysUser = sysUserMapper.selectById(yxOrder.getUserId());
        sysUser.setMoney(sysUser.getMoney().add(yxOrder.getPrice()));
        sysUserMapper.updateById(sysUser);

    }

    /**
     * 管理员给用户办理入住的手续
     */
    @Override
    public void adminOrderDetermine(Long ids) {

        /**
         * 管理员给用户办理入住的手续
         * 1.修改订单的状态为 4 已付款 已入住
         *
         */
        YxOrder yxOrder = yxOrderMapper.selectById(ids);
        yxOrder.setStatus(4);
        yxOrderMapper.updateById(yxOrder);

    }
}
