package com.java.study.modules.banner.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.modules.hotel.dao.YxHotelTypeMapper;
import com.java.study.modules.hotel.entity.YxHotel;
import com.java.study.modules.hotel.entity.YxHotelType;
import com.java.study.modules.hotel.service.YxCommentsService;
import com.java.study.modules.hotel.service.YxHotelService;
import com.java.study.modules.hotel.service.vo.HotelDetailVo;
import com.java.study.modules.sys.service.SysUserService;
import com.java.study.modules.sys.service.dto.UserCountData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 前端接口控制层
 */
@RestController
@RequestMapping("/yx_home")
@Api(tags = {"前端"})
public class YxHomeController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private YxHotelService yxHotelService;
    @Autowired
    private YxHotelTypeMapper yxHotelTypeMapper;

    @Autowired
    private YxCommentsService yxCommentsService;

    /**
     * 统计数据
     */
    @GetMapping("/get_user_count")
    public R getUserCount() {
        UserCountData userCount = sysUserService.getUserCount();
        return R.ok().put("count", userCount);
    }

    /**
     * 分类列表
     */
    @GetMapping("/type_list")
    public R typeList() {
        QueryWrapper<YxHotelType> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(YxHotelType::getIsDel, 0);
        List<YxHotelType> yxHotelTypeList = yxHotelTypeMapper.selectList(wrapper);
        return R.ok().put("list", yxHotelTypeList);
    }

    /**
     * 用户预定查询
     */
    @GetMapping("/reserve_list")
    public R reserveList(@RequestParam Map<String, Object> params) {
        params.put("status", "0");
        PageUtils page = yxHotelService.queryPageReserve(params);
        return R.ok().put("page", page);
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
