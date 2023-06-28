package com.java.study.modules.sys.controller;

import com.java.study.common.constant.Constant;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.common.utils.SecurityUserIdUtils;
import com.java.study.modules.sys.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 系统日志
 */
@Controller
@RequestMapping("/sys/log")
@Api(tags = {"系统：系统日志-管理后台"})
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    /**
     * 列表
     */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("sys:log:list")
    @ApiOperation(value = "日志列表", notes = "")
    public R list(@RequestParam Map<String, Object> params) {
        Long userId = SecurityUserIdUtils.getUserId();
        //只有超级管理员，才能查看所有管理员列表
        if (userId != Constant.SUPER_ADMIN) {
            params.put("createUserId", userId);
        }
        PageUtils page = sysLogService.queryPage(params);
        return R.ok().put("page", page);
    }


    /** 删除 */
    @ResponseBody
    @PostMapping("/delete")
    @RequiresPermissions("sys:log:delete")
    @ApiOperation(value = "删除日志信息")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            sysLogService.removeById(id);
        }
        return R.ok();
    }

}
