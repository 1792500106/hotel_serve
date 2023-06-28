package com.java.study.modules.notice.controller;


import com.java.study.common.annotation.SysLog;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.modules.notice.entity.YxNotice;
import com.java.study.modules.notice.service.YxNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 公告接口控制层
 */
@RestController
@RequestMapping("/yx_notice")
@Api(tags = {"公告-管理后台"})
public class YxNoticeController {


    @Autowired
    private YxNoticeService yxNoticeService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("msg:notice:list")
    @ApiOperation(value = "公告列表")
    @SysLog("查询公告列表")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = yxNoticeService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    @RequiresPermissions("msg:notice:list")
    @ApiOperation(value = "公告单个查找")
    public R info(@PathVariable("id") Integer id) {
        YxNotice resources = yxNoticeService.getById(id);
        return R.ok().put("yxNotice", resources);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("msg:notice:save")
    @ApiOperation(value = "保存")
    @SysLog("保存公告信息")
    public R save(@RequestBody YxNotice resources) {
        yxNoticeService.saveData(resources);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("msg:notice:update")
    @ApiOperation(value = "修改")
    @SysLog("修改公告信息")
    public R update(@RequestBody YxNotice resources) {
        yxNoticeService.updateData(resources);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("msg:notice:delete")
    @ApiOperation(value = "删除")
    @SysLog("删除公告信息")
    public R delete(@RequestBody Long[] ids) {
        yxNoticeService.delData(ids);
        return R.ok();
    }

}
