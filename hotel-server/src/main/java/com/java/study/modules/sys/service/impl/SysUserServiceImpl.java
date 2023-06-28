package com.java.study.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.common.constant.Constant;
import com.java.study.common.exception.RRException;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.common.utils.SecurityUserIdUtils;
import com.java.study.modules.banner.dao.YxBannerMapper;
import com.java.study.modules.banner.entity.YxBanner;
import com.java.study.modules.hotel.dao.YxHotelMapper;
import com.java.study.modules.notice.dao.YxNoticeMapper;
import com.java.study.modules.notice.entity.YxNotice;
import com.java.study.modules.sys.dao.SysUserDao;
import com.java.study.modules.sys.entity.SysUserEntity;
import com.java.study.modules.sys.service.SysRoleService;
import com.java.study.modules.sys.service.SysUserRoleService;
import com.java.study.modules.sys.service.SysUserService;
import com.java.study.modules.sys.service.dto.UserCountData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private YxHotelMapper yxHotelMapper;
    @Autowired
    private YxBannerMapper yxBannerMapper;
    @Autowired
    private YxNoticeMapper yxNoticeMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
//        Long createUserId = (Long) params.get("createUserId");

        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
//                        .eq(createUserId != null, "create_user_id", createUserId)
                        .ne("user_id", 1)
                        .ne("user_id", SecurityUserIdUtils.getUserId())
        );

        return new PageUtils(page);
    }

    @Override
    public List<String> queryAllPerms(Long userId) {
        return baseMapper.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    public SysUserEntity queryByUserName(String username) {
        return baseMapper.queryByUserName(username);
    }

    @Override
    @Transactional
    public void saveUser(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        this.save(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());

    }

    @Override
    @Transactional
    public void update(SysUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }
        this.updateById(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    public void deleteBatch(Long[] userId) {
        this.removeByIds(Arrays.asList(userId));
    }

    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUserEntity user) {
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (user.getCreateUserId() == Constant.SUPER_ADMIN) {
            return;
        }

        //查询用户创建的角色列表
        List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

        //判断是否越权
        if (!roleIdList.containsAll(user.getRoleIdList())) {
            throw new RRException("新增用户所选角色，不是本人创建");
        }
    }

    /**
     * 用户数据统计
     *
     * @return
     */
    @Override
    public UserCountData getUserCount() {
        UserCountData userCountData = new UserCountData();
        Integer sumUserNum = sysUserDao.selectCount(new QueryWrapper<SysUserEntity>());
        userCountData.setSumUserNum(sumUserNum);


        Integer sumTaskNum = yxHotelMapper.countHotel();
        userCountData.setSumTaskNum(sumTaskNum);

        /** 轮播图 */
        QueryWrapper<YxBanner> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(YxBanner::getIsDel,0)
                .orderByDesc(YxBanner::getSort);
        List<YxBanner> yxBanners = yxBannerMapper.selectList(wrapper);
        userCountData.setBannerList(yxBanners);

        QueryWrapper<YxNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(YxNotice::getIsDel,0)
                .orderByDesc(YxNotice::getCreatedTime);
        YxNotice yxNotice = yxNoticeMapper.selectOne(queryWrapper);
        userCountData.setNoticeTile(yxNotice.getTitle());
        userCountData.setNoticeContent(yxNotice.getContent());
        userCountData.setNoticeTime(yxNotice.getCreatedTime());


        return userCountData;
    }
}
