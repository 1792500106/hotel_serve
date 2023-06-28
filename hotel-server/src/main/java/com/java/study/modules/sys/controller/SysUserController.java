package com.java.study.modules.sys.controller;

import com.java.study.common.annotation.SysLog;
import com.java.study.common.constant.Constant;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.R;
import com.java.study.common.validator.ValidatorUtils;
import com.java.study.common.validator.group.AddGroup;
import com.java.study.common.validator.group.UpdateGroup;
import com.java.study.modules.sys.dao.SysUserDao;
import com.java.study.modules.sys.entity.SysUserEntity;
import com.java.study.modules.sys.form.PasswordForm;
import com.java.study.modules.sys.service.SysUserRoleService;
import com.java.study.modules.sys.service.SysUserService;
import com.java.study.modules.sys.service.dto.UserCountData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = {"系统：管理后台用户-管理后台"})
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserDao sysUserDao;

    /**
     * 所有用户列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    @ApiOperation(value = "用户列表", notes = "")
    public R list(@RequestParam Map<String, Object> params) {
//        Long userId = SecurityUserIdUtils.getUserId();
//        //只有超级管理员，才能查看所有管理员列表
//        if (userId != Constant.SUPER_ADMIN) {
//            params.put("createUserId", userId);
//        }
        PageUtils page = sysUserService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    @ApiOperation(value = "登录用户信息", notes = "")
    public R info() {
        return R.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    @ApiOperation(value = "修改密码", notes = "")
    public R password(@RequestBody PasswordForm form) {
        Assert.hasText(form.getNewPassword(), "新密码不为能空");

        //sha256加密
        String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    @ApiOperation(value = "用户信息", notes = "")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.getById(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    @ApiOperation(value = "保存用户", notes = "")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        user.setCreateUserId(getUserId());
        sysUserService.saveUser(user);

        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    @ApiOperation(value = "删除用户", notes = "")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        user.setCreateUserId(getUserId());
        sysUserService.update(user);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    @ApiOperation(value = "删除用户", notes = "")
    public R delete(@RequestBody Long[] userIds) {
        if (Arrays.stream(userIds).anyMatch(id -> id.intValue() == Constant.SUPER_ADMIN)) {
            return R.error("系统管理员不能删除");
        }
        if (Arrays.stream(userIds).anyMatch(id -> getUserId().equals(id))) {
            return R.error("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return R.ok();
    }

    @GetMapping("/msg_info")
    @ApiOperation(value = "单个查找")
    public R userMsgInfo() {
        SysUserEntity sysUserEntity = sysUserDao.selectById(getUserId());
        return R.ok().put("userMsg", sysUserEntity);
    }

    @PostMapping("/msg_update")
    @ApiOperation(value = "修改")
    @SysLog("修改用户个人资料信息")
    public R userMsgUpdate(@RequestBody SysUserEntity resources) {
        sysUserDao.updateById(resources);
        return R.ok();
    }


    @PostMapping("/register")
    @ApiOperation("用户注册")
    public R register(@RequestBody SysUserEntity user) {
        SysUserEntity sysUserEntity = sysUserService.queryByUserName(user.getUsername());
        if (sysUserEntity != null) {
            return R.error("账号重复了！");
        }
        //学生角色
        List<Long> roleIdList = new ArrayList<>();
        roleIdList.add(2L);
        ValidatorUtils.validateEntity(user, AddGroup.class);
        user.setCreateUserId(1L);
        user.setRoleIdList(roleIdList);
        user.setStatus(1);
        user.setMoney(new BigDecimal(0));
        user.setCreateTime(new Date());
        sysUserService.saveUser(user);
        return R.ok();
    }

    /**
     * 用户数据统计
     */
    @GetMapping("/get_user_count")
    @ApiOperation(value = "用户数据统计")
    public R getUserCount() {
        UserCountData userCount = sysUserService.getUserCount();
        return R.ok().put("count", userCount);
    }
}
