package com.java.study.modules.hotel.controller;


import com.java.study.common.annotation.SysLog;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.modules.hotel.entity.YxHotelType;
import com.java.study.modules.hotel.service.YxHotelTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 酒店分类管理接口控制层
 */
@RestController
@RequestMapping("/yx_hotel_type")
@Api(tags = {"酒店分类管理-管理后台"})
public class YxHotelTypeController {


    @Autowired
    private YxHotelTypeService yxHotelTypeService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("hotel:type:list")
    @ApiOperation(value = "酒店分类列表")
    @SysLog("查询酒店分类列表")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = yxHotelTypeService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    @RequiresPermissions("hotel:type:list")
    @ApiOperation(value = "酒店分类单个查找")
    public R info(@PathVariable("id") Integer id) {
        YxHotelType resources = yxHotelTypeService.getById(id);
        return R.ok().put("yxHotelType", resources);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("hotel:type:save")
    @ApiOperation(value = "保存")
    @SysLog("保存酒店分类信息")
    public R save(@RequestBody YxHotelType resources) {
        yxHotelTypeService.saveData(resources);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("hotel:type:update")
    @ApiOperation(value = "修改")
    @SysLog("修改酒店分类信息")
    public R update(@RequestBody YxHotelType resources) {
        yxHotelTypeService.updateData(resources);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("hotel:type:delete")
    @ApiOperation(value = "删除")
    @SysLog("删除酒店分类信息")
    public R delete(@RequestBody Long[] ids) {
        yxHotelTypeService.delData(ids);
        return R.ok();
    }

}
