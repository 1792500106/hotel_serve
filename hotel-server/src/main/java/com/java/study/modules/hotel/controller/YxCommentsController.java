package com.java.study.modules.hotel.controller;


import com.java.study.common.annotation.SysLog;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.modules.hotel.entity.YxComments;
import com.java.study.modules.hotel.service.YxCommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 评论管理接口控制层
 */
@RestController
@RequestMapping("/yx_comments")
@Api(tags = {"评论管理-管理后台"})
public class YxCommentsController {


    @Autowired
    private YxCommentsService yxCommentsService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("msg:comments:list")
    @ApiOperation(value = "评论管理列表")
    @SysLog("查询评论管理列表")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = yxCommentsService.queryAdminPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    @RequiresPermissions("msg:comments:list")
    @ApiOperation(value = "评论管理单个查找")
    public R info(@PathVariable("id") Integer id) {
        YxComments resources = yxCommentsService.getById(id);
        return R.ok().put("yxComments", resources);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("msg:comments:save")
    @ApiOperation(value = "保存")
    @SysLog("保存评论管理信息")
    public R save(@RequestBody YxComments resources) {
        yxCommentsService.saveData(resources);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("msg:comments:update")
    @ApiOperation(value = "修改")
    @SysLog("修改评论管理信息")
    public R update(@RequestBody YxComments resources) {
        yxCommentsService.updateData(resources);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("msg:comments:delete")
    @ApiOperation(value = "删除")
    @SysLog("删除评论管理信息")
    public R delete(@RequestBody Long[] ids) {
        yxCommentsService.delData(ids);
        return R.ok();
    }

}
