package com.java.study.modules.hotel.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import com.java.study.common.exception.RRException;
import com.java.study.common.utils.DateUtils;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.common.utils.SecurityUserIdUtils;
import com.java.study.modules.hotel.dao.YxHotelMapper;
import com.java.study.modules.hotel.entity.YxHotel;
import com.java.study.modules.hotel.service.YxHotelService;
import com.java.study.modules.hotel.service.vo.ShowHotelMoneyVo;
import com.java.study.modules.hotel.service.vo.UserReserveVo;
import com.java.study.modules.order.dao.YxOrderMapper;
import com.java.study.modules.order.entity.YxOrder;
import com.java.study.modules.personal.dao.SysUserMapper;
import com.java.study.modules.personal.entity.SysUser;
import com.java.study.modules.personal.service.YxExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service 实现类层
 */
@Service
public class YxHotelServiceImpl extends ServiceImpl<YxHotelMapper, YxHotel> implements YxHotelService {

    /**
     * 查询文章列表时返回的字段（过滤掉详情字段以加快速度）
     */
    private static final String LIST_FILEDS = "*";

    @Autowired
    private YxHotelMapper yxHotelMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private YxOrderMapper yxOrderMapper;
    @Autowired
    private YxExpenseService yxExpenseService;

    @Override
    public PageUtils queryPageList(Map<String, Object> params) {
        String title = (String) params.get("title");
        String typeId = (String) params.get("typeId");
        IPage<YxHotel> page = this.page(
                new Query<YxHotel>().getPage(params),
                new QueryWrapper<YxHotel>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "title", title)
                        .eq("is_del", 0)
                        .eq(StringUtils.hasText(typeId), "type_id", typeId)
        );
        return new PageUtils(page);
    }


    @Override
    public PageUtils queryPageReserve(Map<String, Object> params) {
        String title = (String) params.get("title");
        String typeId = (String) params.get("typeId");
        String status = (String) params.get("status");
        String sort = (String) params.get("sort");

        QueryWrapper<YxHotel> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(YxHotel::getIsDel, 0);
        if (StringUtils.hasText(title)) {
            wrapper.lambda()
                    .like(YxHotel::getHomeName, title);
        }
        if (StringUtils.hasText(typeId)) {
            wrapper.lambda()
                    .eq(YxHotel::getTypeId, typeId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.lambda()
                    .eq(YxHotel::getStatus, status);
        }
        if (StringUtils.hasText(sort)) {
            if (sort.equals("1")) {
                wrapper.lambda()
                        .orderByAsc(YxHotel::getNewPrice);
            }
            if (sort.equals("2")) {
                wrapper.lambda()
                        .orderByDesc(YxHotel::getNewPrice);
            }
            if (sort.equals("3")) {
                wrapper.lambda()
                        .orderByDesc(YxHotel::getNumberOf);
            }
        }
        List<YxHotel> yxHotelList = yxHotelMapper.selectList(wrapper);

        IPage<YxHotel> page1 = new Query<YxHotel>().getPage(params);
        page1.setRecords(yxHotelList);

        return new PageUtils(page1);
    }

    /**
     * 保存
     */
    @Override
    public void saveData(YxHotel resources) {
        resources.setCreatedTime(new Date());
        resources.setIsDel(0);
        resources.setStatus(0);
        yxHotelMapper.insert(resources);
    }

    /**
     * 修改
     */
    @Override
    public void updateData(YxHotel resources) {
        yxHotelMapper.updateById(resources);
    }

    /**
     * 删除
     */
    @Override
    public void delData(Long[] ids) {
        for (Long id : ids) {
            YxHotel resources = new YxHotel();
            resources.setId(id);
            resources.setIsDel(1);
            yxHotelMapper.updateById(resources);
        }
    }


    /**
     * 用户订房
     */
    @Override
    public void userReserve(UserReserveVo resources) {

        /**
         * 订房流程
         * 1.查询当前登录用户的信息
         * 2.计算用户订房几天
         * 3.计算用户订房需要的费用
         * 4.判断费用是否够用
         *
         * */
        SysUser sysUser = sysUserMapper.selectById(SecurityUserIdUtils.getUserId());

        if (!StringUtil.isNotEmpty(sysUser.getUserCard())) {
            throw new RRException("请选完善个人信息！");
        }
        if (!StringUtil.isNotEmpty(sysUser.getNickName())) {
            throw new RRException("请选完善个人信息！");
        }


        int days = DateUtils.differentDaysByMillisecond(resources.getStartTime(), resources.getEndTime());

        if (days > 0) {
            YxHotel yxHotel = yxHotelMapper.selectById(resources.getId());
            BigDecimal newPrice = yxHotel.getNewPrice();
            BigDecimal daysBigDecimal = new BigDecimal(days);

            /** 最终要给钱 */
            BigDecimal userShiJiFangMoney = newPrice.multiply(daysBigDecimal);

            /** 用户的钱  减去房间的钱 */
            BigDecimal userShengYuMoney = sysUser.getMoney().subtract(userShiJiFangMoney);
            BigDecimal subtractFor = new BigDecimal(0);

            /** 判断金钱是否够用 */
            if (userShengYuMoney.compareTo(subtractFor) > 0) {

                /** 插入订单 */
                YxOrder yxOrder = new YxOrder();
                yxOrder.setUserId(sysUser.getUserId());
                yxOrder.setUserName(sysUser.getNickName());
                yxOrder.setHomeId(yxHotel.getId());
                yxOrder.setHomeNum(yxHotel.getHomeNum());
                yxOrder.setOrderId("" + IdWorker.getId());
                yxOrder.setStartTime(resources.getStartTime());
                yxOrder.setEndTime(resources.getEndTime());
                yxOrder.setStartDay(days);
                yxOrder.setPrice(userShiJiFangMoney);
                yxOrder.setStatus(1);
                yxOrder.setTrips(resources.getTrips());
                yxOrder.setHotelTypeId(yxHotel.getTypeId());
                yxOrder.setIsExtended(0);
                yxOrder.setUserCard(sysUser.getUserCard());
                if (StringUtil.isNotEmpty(resources.getRemarks())) {
                    yxOrder.setRemarks(resources.getRemarks());
                }
                yxOrder.setCreatedTime(new Date());
                yxOrder.setIsDel(0);
                yxOrderMapper.insert(yxOrder);

                /** 插入流水 */
                yxExpenseService.saveUserExpense(sysUser.getUserId(), sysUser.getNickName(), "订购房间：" + yxHotel.getHomeName() + ",一共：" + days + "天", "消费", userShiJiFangMoney);

                /** 修改房间状态 */
                yxHotel.setStatus(1);
                yxHotel.setNumberOf(yxHotel.getNumberOf() + 1);
                yxHotelMapper.updateById(yxHotel);

                /** 修改用户余额 */
                sysUser.setMoney(userShengYuMoney);
                sysUserMapper.updateById(sysUser);

            } else {
                throw new RRException("余额不足！");
            }
        } else {
            throw new RRException("时间选择有问题！");
        }


    }

    @Override
    public void adminReserve(UserReserveVo resources) {

        int days = DateUtils.differentDaysByMillisecond(resources.getStartTime(), resources.getEndTime());

        if (days > 0) {
            YxHotel yxHotel = yxHotelMapper.selectById(resources.getId());
            BigDecimal newPrice = yxHotel.getNewPrice();
            BigDecimal daysBigDecimal = new BigDecimal(days);

            /** 最终要给钱 */
            BigDecimal userShiJiFangMoney = newPrice.multiply(daysBigDecimal);


            /** 插入订单 */
            YxOrder yxOrder = new YxOrder();
            yxOrder.setUserId(0L);
            yxOrder.setUserName(resources.getUserName());
            yxOrder.setHomeId(yxHotel.getId());
            yxOrder.setHomeNum(yxHotel.getHomeNum());
            yxOrder.setOrderId("" + IdWorker.getId());
            yxOrder.setStartTime(resources.getStartTime());
            yxOrder.setEndTime(resources.getEndTime());
            yxOrder.setStartDay(days);
            yxOrder.setPrice(userShiJiFangMoney);
            yxOrder.setStatus(1);
            yxOrder.setIsExtended(0);
            yxOrder.setTrips(resources.getTrips());
            yxOrder.setHotelTypeId(yxHotel.getTypeId());
            yxOrder.setUserCard(resources.getUserCard());
            if (StringUtil.isNotEmpty(resources.getRemarks())) {
                yxOrder.setRemarks(resources.getRemarks());
            }
            yxOrder.setCreatedTime(new Date());
            yxOrder.setIsDel(0);
            yxOrderMapper.insert(yxOrder);


            /** 修改房间状态 */
            yxHotel.setStatus(1);
            yxHotel.setNumberOf(yxHotel.getNumberOf() + 1);
            yxHotelMapper.updateById(yxHotel);

        } else {
            throw new RRException("时间选择有问题！");
        }
    }

    /**
     * 订房计算金额
     */
    @Override
    public ShowHotelMoneyVo hotelMoney(UserReserveVo resources) {
        ShowHotelMoneyVo showHotelMoneyVo = new ShowHotelMoneyVo();

        int days = DateUtils.differentDaysByMillisecond(resources.getStartTime(), resources.getEndTime());

        if (days > 0) {
            YxHotel yxHotel = yxHotelMapper.selectById(resources.getId());
            BigDecimal newPrice = yxHotel.getNewPrice();
            BigDecimal daysBigDecimal = new BigDecimal(days);

            /** 最终要给钱 */
            BigDecimal userShiJiFangMoney = newPrice.multiply(daysBigDecimal);
            showHotelMoneyVo.setHotelMoney(userShiJiFangMoney);
        } else {
            showHotelMoneyVo.setHotelMoney(new BigDecimal(0));
        }
        return showHotelMoneyVo;
    }
}
