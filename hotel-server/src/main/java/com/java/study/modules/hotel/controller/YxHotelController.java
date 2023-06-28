package com.java.study.modules.hotel.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.study.common.annotation.SysLog;
import com.java.study.common.exception.RRException;
import com.java.study.common.utils.DateUtils;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.modules.hotel.dao.YxHotelTypeMapper;
import com.java.study.modules.hotel.entity.YxHotel;
import com.java.study.modules.hotel.entity.YxHotelType;
import com.java.study.modules.hotel.service.YxCommentsService;
import com.java.study.modules.hotel.service.YxHotelService;
import com.java.study.modules.hotel.service.vo.HotelDetailVo;
import com.java.study.modules.hotel.service.vo.ShowHotelMoneyVo;
import com.java.study.modules.hotel.service.vo.UserReserveVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 酒店管理接口控制层
 */
@RestController
@RequestMapping("/yx_hotel")
@Api(tags = {"酒店管理-管理后台"})
public class YxHotelController {


    @Autowired
    private YxHotelService yxHotelService;
    @Autowired
    private YxHotelTypeMapper yxHotelTypeMapper;
    @Autowired
    private YxCommentsService yxCommentsService;


    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("msg:hotel:list")
    @ApiOperation(value = "酒店列表")
    @SysLog("查询酒店列表")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = yxHotelService.queryPageList(params);
        return R.ok().put("page", page);
    }

    /**
     * 用户预定查询
     */
    @GetMapping("/reserve_list")
    @ApiOperation(value = "酒店列表")
    public R reserveList(@RequestParam Map<String, Object> params) {
        params.put("status", "0");
        PageUtils page = yxHotelService.queryPageReserve(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    @RequiresPermissions("msg:hotel:list")
    @ApiOperation(value = "酒店单个查找")
    public R info(@PathVariable("id") Integer id) {
        YxHotel resources = yxHotelService.getById(id);
        return R.ok().put("yxHotel", resources);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("msg:hotel:save")
    @ApiOperation(value = "保存")
    @SysLog("保存酒店信息")
    public R save(@RequestBody YxHotel resources) {
        yxHotelService.saveData(resources);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("msg:hotel:update")
    @ApiOperation(value = "修改")
    @SysLog("修改酒店信息")
    public R update(@RequestBody YxHotel resources) {
        yxHotelService.updateData(resources);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("msg:hotel:delete")
    @ApiOperation(value = "删除")
    @SysLog("删除酒店信息")
    public R delete(@RequestBody Long[] ids) {
        yxHotelService.delData(ids);
        return R.ok();
    }

    /**
     * 分类
     */
    @GetMapping("/type_list")
    @ApiOperation(value = "分类列表")
    public R typeList() {
        QueryWrapper<YxHotelType> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(YxHotelType::getIsDel, 0);
        List<YxHotelType> yxHotelTypeList = yxHotelTypeMapper.selectList(wrapper);
        return R.ok().put("list", yxHotelTypeList);
    }


    /**
     * 用户订房
     */
    @PostMapping("/user_reserve")
    @RequiresPermissions("user:reserve:save")
    @ApiOperation(value = "保存")
    @SysLog("用户订房")
    public R userReserve(@RequestBody UserReserveVo resources) throws Exception {

        String[] startTimes = resources.getStartTimes();
        Date startTime = DateUtils.stringToDate(startTimes[0]);
        Date endTime = DateUtils.stringToDate(startTimes[1]);
        resources.setStartTime(startTime);
        resources.setEndTime(endTime);

        /** 判断用户提交的时间是否符合要求 */
        if (resources.getEndTime() == null) {
            throw new RRException("时间不能为空！");
        }
        if (resources.getStartTime() == null) {
            throw new RRException("时间不能为空！");
        }

        boolean days = startTime.before(endTime);
        System.out.println(days);
        if (!days) {
            throw new RRException("结束时间不能小于开始时间！");
        }
        Date nowDate = DateUtils.addDateDays(new Date(), 1);

//        Date nowTime = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String format = formatter.format(nowDate);
//        String retroactiveTime = format + " 00:00:00";
//        Date toForTime = DateUtils.stringToDate(retroactiveTime);
//        boolean daysFor = startTime.before(toForTime);
//        System.out.println(daysFor);
//        if (daysFor) {
//            throw new RRException("开始时间要大于当前时间！");
//        }

        yxHotelService.userReserve(resources);
        return R.ok();
    }

    /**
     * 管理员订房
     */
    @PostMapping("/admin_reserve")
    @RequiresPermissions("admin:reserve:save")
    @ApiOperation(value = "保存")
    @SysLog("管理员订房")
    public R adminReserve(@RequestBody UserReserveVo resources) throws Exception {

        String[] startTimes = resources.getStartTimes();
        Date startTime = DateUtils.stringToDate(startTimes[0]);
        Date endTime = DateUtils.stringToDate(startTimes[1]);
        resources.setStartTime(startTime);
        resources.setEndTime(endTime);

        /** 判断用户提交的时间是否符合要求 */
        if (resources.getEndTime() == null) {
            throw new RRException("时间不能为空！");
        }
        if (resources.getStartTime() == null) {
            throw new RRException("时间不能为空！");
        }

        boolean days = startTime.before(endTime);
        System.out.println(days);
        if (!days) {
            throw new RRException("结束时间不能小于开始时间！");
        }
        Date nowDate = DateUtils.addDateDays(new Date(), 1);
        boolean daysFor = startTime.before(nowDate);

        System.out.println(daysFor);
        if (!daysFor) {
            throw new RRException("开始时间不能小于当前时间！");
        }

        yxHotelService.adminReserve(resources);
        return R.ok();
    }

    /**
     * 用户订房计算金额
     */
    @PostMapping("/hotel_money")
    @ApiOperation(value = "用户订房计算金额")
    @SysLog("用户订房计算金额")
    public R hotelMoney(@RequestBody UserReserveVo resources) throws Exception {
        String[] startTimes = resources.getStartTimes();
        Date startTime = DateUtils.stringToDate(startTimes[0]);
        Date endTime = DateUtils.stringToDate(startTimes[1]);
        resources.setStartTime(startTime);
        resources.setEndTime(endTime);
        ShowHotelMoneyVo showHotelMoneyVo = yxHotelService.hotelMoney(resources);
        return R.ok().put("forMoney", showHotelMoneyVo);
    }


    @GetMapping("/hotel_detail")
    @ApiOperation(value = "房间详情")
    public R hotelDetail(@RequestParam Map<String, Object> params) {

        String hotelId = (String) params.get("hotelId");
        long itemId = Long.parseLong(hotelId);
        HotelDetailVo hotelDetailVo = new HotelDetailVo();

        YxHotel resources = yxHotelService.getById(itemId);
        hotelDetailVo.setYxHotel(resources);
        PageUtils pageUtils = yxCommentsService.queryAdminPage(params);

        hotelDetailVo.setPageUtils(pageUtils);

        return R.ok().put("page", hotelDetailVo);
    }

}
