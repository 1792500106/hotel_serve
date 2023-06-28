package com.java.study.modules.order.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.study.common.annotation.SysLog;
import com.java.study.common.exception.RRException;
import com.java.study.common.utils.DateUtils;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.common.utils.SecurityUserIdUtils;
import com.java.study.config.AlipayConfig;
import com.java.study.modules.hotel.dao.YxHotelTypeMapper;
import com.java.study.modules.hotel.entity.YxHotelType;
import com.java.study.modules.order.entity.YxOrder;
import com.java.study.modules.order.service.YxOrderService;
import com.java.study.modules.order.service.dto.GetOrderCountListDto;
import com.java.study.modules.order.service.vo.AdminExtendVo;
import com.java.study.modules.personal.dao.SysUserMapper;
import com.java.study.modules.personal.entity.SysUser;
import com.java.study.modules.personal.entity.YxExpense;
import com.java.study.modules.personal.service.YxExpenseService;
import com.java.study.modules.personal.service.vo.UserRechargeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.ToString;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 订单接口控制层
 */
@RestController
@RequestMapping("/yx_order")
@Api(tags = {"订单-管理后台"})
public class YxOrderController {


    @Autowired
    private YxOrderService yxOrderService;
    @Autowired
    private YxHotelTypeMapper yxHotelTypeMapper;

    @Resource
    private AlipayConfig alipayConfig;

    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private YxExpenseService yxExpenseService;
    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("msg:order:list")
    @ApiOperation(value = "订单列表")
    @SysLog("查询订单列表")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = yxOrderService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 离房结算
     */
    @PostMapping("/check")
    @RequiresPermissions("msg:order:check")
    @ApiOperation(value = "管理员离房结算")
    @SysLog("管理员离房结算")
    public R check(@RequestBody Long[] ids) {
        yxOrderService.check(ids);
        return R.ok();
    }


    /**
     * 管理员延长开房时间
     */
    @PostMapping("/admin_extend")
    @RequiresPermissions("msg:order:extend")
    @ApiOperation(value = "管理员延长开房时间")
    @SysLog("管理员延长开房时间")
    public R adminOrderExtend(@RequestBody AdminExtendVo resources) throws Exception {

        Date endTime = DateUtils.stringToDate(resources.getEndTime());
        resources.setEndTimeFor(endTime);
        if (endTime == null) {
            throw new RRException("时间不能为空！");
        }

//        Date nowDate = DateUtils.addDateDays(new Date(), 1);
        Date nowDate = new Date();
        boolean daysFor = endTime.before(nowDate);

        System.out.println(daysFor);
        if (daysFor) {
            throw new RRException("时间不能小于当前时间！");
        }

        yxOrderService.adminOrderExtend(resources);
        return R.ok();
    }

    /**
     * 用户充值
     */
    @PostMapping("/user_recharge")
    @ResponseBody
    @ApiOperation(value = "用户充值")
    @SysLog("用户充值")
    public R userRecharge(@RequestBody UserRechargeVo resources) {
        return R.ok().put("url", yxOrderService.userRecharge(resources));
    }

    /**
     * 统计订单
     */
    @GetMapping("/order_count")
    @ApiOperation(value = "统计订单")
    public R orderCount() {
        GetOrderCountListDto page = yxOrderService.orderCount();
        return R.ok().put("page", page);
    }

    @ResponseBody
    @RequestMapping("/paysuccess")
    public String paySuccess(PayAsyncVo vo, HttpServletRequest request) throws AlipayApiException {
        System.out.println("===================");
        boolean signVerified = checkV1(request);
        if(signVerified){
            String out_trade_no = vo.getOut_trade_no();
            /** 修改用户金额 */
//            YxOrder yxOrder = yxOrderService.getById(out_trade_no);
            YxExpense yxExpense = yxExpenseService.getById(out_trade_no);
            Long userId = yxExpense.getUserId();
            SysUser sysUser = sysUserMapper.selectById(userId);
            sysUser.setMoney(sysUser.getMoney().add(yxExpense.getPrice()));
            sysUserMapper.updateById(sysUser);
            return "true";
        }else{
            return "error";
        }
    }

    private boolean checkV1(HttpServletRequest request) throws AlipayApiException {
        /*
         * 支付宝验证签名
         * 获取支付宝POST过来反馈信息
         */
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        // 调用SDK验证签名
        return AlipaySignature.rsaCheckV1(params, alipayConfig.ALIPAY_PUBLIC_KEY, alipayConfig.CHARSET, alipayConfig.SIGNTYPE);
    }

    @ToString
    @Data
    public class PayAsyncVo {
        private String gmt_create;
        private String charset;
        private String gmt_payment;
        private String notify_time;
        private String subject;
        private String sign;
        /** 支付者的id */
        private String buyer_id;
        /** 订单的信息 */
        private String body;
        /** 支付金额 */
        private String invoice_amount;
        private String version;
        /** 通知id */
        private String notify_id;
        private String fund_bill_list;
        /** 通知类型； trade_status_sync */
        private String notify_type;
        /** 订单号 */
        private String out_trade_no;
        /** 支付的总额 */
        private String total_amount;
        /** 交易状态  TRADE_SUCCESS */
        private String trade_status;
        /** 流水号 */
        private String trade_no;
        private String auth_app_id;
        /** 商家收到的款 */
        private String receipt_amount;
        private String point_amount;
        /** 应用id */
        private String app_id;
        /** 最终支付的金额 */
        private String buyer_pay_amount;
        /** 签名类型 */
        private String sign_type;
        /** 商家的id */
        private String seller_id;
    }


    @PostMapping("/order_quit")
    @RequiresPermissions("msg:order:quit")
    @ApiOperation(value = "管理员给用户退房")
    @SysLog("管理员给用户退房")
    public R adminOrderQuit(@RequestBody Long ids) {
        yxOrderService.adminOrderQuit(ids);
        return R.ok();
    }

    @PostMapping("/order_determine")
    @RequiresPermissions("msg:order:determine")
    @ApiOperation(value = "管理员办理入住手续")
    @SysLog("管理员办理入住手续")
    public R adminOrderDetermine(@RequestBody Long ids) {
        yxOrderService.adminOrderDetermine(ids);
        return R.ok();
    }

    /**
     * 房间分类
     */
    @GetMapping("/type_list")
    @ApiOperation(value = "房间分类")
    public R typeList() {
        QueryWrapper<YxHotelType> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(YxHotelType::getIsDel, 0);
        List<YxHotelType> yxHotelTypeList = yxHotelTypeMapper.selectList(wrapper);
        return R.ok().put("list", yxHotelTypeList);
    }


}
