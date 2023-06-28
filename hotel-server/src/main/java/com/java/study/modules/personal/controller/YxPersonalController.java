package com.java.study.modules.personal.controller;


import com.java.study.common.annotation.SysLog;
import com.java.study.common.exception.RRException;
import com.java.study.common.utils.DateUtils;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.common.utils.SecurityUserIdUtils;
import com.java.study.modules.hotel.entity.YxComments;
import com.java.study.modules.hotel.service.YxCommentsService;
import com.java.study.modules.order.service.YxOrderService;
import com.java.study.modules.order.service.vo.UserExtendVo;
import com.java.study.modules.personal.dao.SysUserMapper;
import com.java.study.modules.personal.entity.SysUser;
import com.java.study.modules.personal.service.vo.UserCommentsHotel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


/**
 * 个人中心管理接口控制层
 */
@RestController
@RequestMapping("/yx_personal")
@Api(tags = {"个人中心管理-管理后台"})
public class YxPersonalController {


    @Autowired
    private YxOrderService yxOrderService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private YxCommentsService yxCommentsService;

    /**
     * 用户个人订单列表
     */
    @GetMapping("/user_order")
    @ApiOperation(value = "用户个人订单列表")
    public R userOrder(@RequestParam Map<String, Object> params) {
        Long userId = SecurityUserIdUtils.getUserId();
        params.put("createUserId", userId);
        PageUtils page = yxOrderService.userOrderList(params);
        return R.ok().put("page", page);
    }

    /**
     * 用户删除个人订单
     */
    @PostMapping("/user_order_delete")
    @RequiresPermissions("my:order:delete")
    @ApiOperation(value = "删除")
    @SysLog("用户删除个人订单")
    public R delete(@RequestBody Long[] ids) {
        yxOrderService.delData(ids);
        return R.ok();
    }


    /**
     * 用户房前房间列表
     */
    @GetMapping("/user_home")
    @ApiOperation(value = "用户房前房间列表")
    public R userHome(@RequestParam Map<String, Object> params) {
        Long userId = SecurityUserIdUtils.getUserId();
        params.put("createUserId", userId);
        PageUtils page = yxOrderService.userHome(params);
        return R.ok().put("page", page);
    }

    /**
     * 用户自己延长开房时间
     */
    @PostMapping("/user_extend")
    @RequiresPermissions("user:home:extend")
    @ApiOperation(value = "用户延长开房时间")
    @SysLog("用户延长开房时间")
    public R userOrderExtend(@RequestBody UserExtendVo resources) throws Exception {

        Date endTime = DateUtils.stringToDate(resources.getEndTime());
        resources.setEndTimeFor(endTime);
        if (endTime == null) {
            throw new RRException("时间不能为空！");
        }

        Date nowDate = new Date();
        boolean daysFor = endTime.before(nowDate);

        System.out.println(daysFor);
        if (daysFor) {
            throw new RRException("时间不能小于当前时间！");
        }

        yxOrderService.userOrderExtend(resources);
        return R.ok();
    }

    @GetMapping("/user_info")
    @ApiOperation(value = "用户个人信息")
    @SysLog("用户个人信息")
    public R userInfo() {
        SysUser sysUser = sysUserMapper.selectById(SecurityUserIdUtils.getUserId());
        return R.ok().put("user", sysUser);
    }

    @PostMapping("/user_order_quit")
    @RequiresPermissions("user:home:quit")
    @ApiOperation(value = "用户个人退房")
    @SysLog("用户个人退房")
    public R userOrderQuit(@RequestBody Long ids) {
        yxOrderService.userOrderQuit(ids);
        return R.ok();
    }


    /**
     * 用户添加房间评论信息
     */
    @PostMapping("/order_comments")
    @RequiresPermissions("my:order:comments")
    @ApiOperation(value = "用户添加房间评论信息")
    @SysLog("用户添加房间评论信息")
    public R userCommentsHotel(@RequestBody UserCommentsHotel resources) {
        yxCommentsService.userCommentsHotel(resources);
        return R.ok();
    }

    @GetMapping("/user_comments")
    @ApiOperation(value = "用户查看个人评论")
    @SysLog("用户查看个人评论")
    public R userComments(@RequestParam Map<String, Object> params) {

        Long userId = SecurityUserIdUtils.getUserId();
        params.put("userId", userId);
        PageUtils page = yxCommentsService.queryUserPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 用户删除个人评论
     */
    @PostMapping("/comments_delete")
    @RequiresPermissions("my:comments:delete")
    @ApiOperation(value = "用户删除个人评论")
    @SysLog("用户删除个人评论")
    public R commentDelete(@RequestBody Long[] ids) {
        yxCommentsService.delData(ids);
        return R.ok();
    }

    /**
     * 用户个人修改评论
     */
    @PostMapping("/comments_update")
    @RequiresPermissions("my:comments:update")
    @ApiOperation(value = "用户个人修改评论")
    @SysLog("用户个人修改评论")
    public R update(@RequestBody YxComments resources) {
        yxCommentsService.updateData(resources);
        return R.ok();
    }

}
