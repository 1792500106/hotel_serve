package com.java.study.modules.personal.controller;


import com.java.study.common.annotation.SysLog;
import com.java.study.common.constant.Constant;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.common.utils.SecurityUserIdUtils;
import com.java.study.modules.personal.entity.YxExpense;
import com.java.study.modules.personal.service.YxExpenseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 账务流水管理接口控制层
 */
@RestController
@RequestMapping("/yx_expense")
@Api(tags = {"账务流水管理-管理后台"})
public class YxExpenseController {


    @Autowired
    private YxExpenseService yxExpenseService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("msg:expense:list")
    @ApiOperation(value = "账务流水列表")
    @SysLog("查询账务流水列表")
    public R list(@RequestParam Map<String, Object> params) {
        Long userId = SecurityUserIdUtils.getUserId();
        //只有超级管理员，才能查看所有管理员列表
        if (userId != Constant.SUPER_ADMIN) {
            params.put("createUserId", userId);
        }
        PageUtils page = yxExpenseService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    @RequiresPermissions("msg:expense:list")
    @ApiOperation(value = "账务流水单个查找")
    public R info(@PathVariable("id") Integer id) {
        YxExpense resources = yxExpenseService.getById(id);
        return R.ok().put("yxExpense", resources);
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("msg:expense:delete")
    @ApiOperation(value = "删除")
    @SysLog("删除账务流水信息")
    public R delete(@RequestBody Long[] ids) {
        yxExpenseService.delData(ids);
        return R.ok();
    }

}
