package com.java.study.modules.banner.controller;


import com.java.study.common.annotation.SysLog;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.modules.banner.entity.YxBanner;
import com.java.study.modules.banner.service.YxBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 轮播图接口控制层
 */
@RestController
@RequestMapping("/yx_banner")
@Api(tags = {"轮播图-管理后台"})
public class YxBannerController {


    @Autowired
    private YxBannerService yxBannerService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("msg:banner:list")
    @ApiOperation(value = "轮播图列表")
//    @SysLog("查询轮播图")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = yxBannerService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    @RequiresPermissions("msg:banner:list")
    @ApiOperation(value = "轮播图单个查找")
    public R info(@PathVariable("id") Integer id) {
        YxBanner resources = yxBannerService.getById(id);
        return R.ok().put("yxBanner", resources);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("msg:banner:save")
    @ApiOperation(value = "轮播图保存")
    @SysLog("保存轮播图信息")
    public R save(@RequestBody YxBanner resources) {
        yxBannerService.saveData(resources);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("msg:banner:update")
    @ApiOperation(value = "修改")
    @SysLog("修改轮播图信息")
    public R update(@RequestBody YxBanner resources) {
        yxBannerService.updateData(resources);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("msg:banner:delete")
    @ApiOperation(value = "删除")
    @SysLog("删除轮播图信息")
    public R delete(@RequestBody Long[] ids) {
        yxBannerService.delData(ids);
        return R.ok();
    }

}
