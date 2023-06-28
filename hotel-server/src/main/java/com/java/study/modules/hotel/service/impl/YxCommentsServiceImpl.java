package com.java.study.modules.hotel.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.common.utils.SecurityUserIdUtils;
import com.java.study.modules.hotel.dao.YxCommentsMapper;
import com.java.study.modules.hotel.dao.YxHotelMapper;
import com.java.study.modules.hotel.entity.YxComments;
import com.java.study.modules.hotel.entity.YxHotel;
import com.java.study.modules.hotel.service.YxCommentsService;
import com.java.study.modules.order.dao.YxOrderMapper;
import com.java.study.modules.order.entity.YxOrder;
import com.java.study.modules.personal.dao.SysUserMapper;
import com.java.study.modules.personal.entity.SysUser;
import com.java.study.modules.personal.service.vo.UserCommentsHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Service 实现类层
 */
@Service
public class YxCommentsServiceImpl extends ServiceImpl<YxCommentsMapper, YxComments> implements YxCommentsService {

    /**
     * 查询文章列表时返回的字段（过滤掉详情字段以加快速度）
     */
    private static final String LIST_FILEDS = "*";

    @Autowired
    private YxCommentsMapper yxCommentsMapper;
    @Autowired
    private YxHotelMapper yxHotelMapper;
    @Autowired
    private YxOrderMapper yxOrderMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public PageUtils queryAdminPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        String hotelId = (String) params.get("hotelId");
        IPage<YxComments> page = this.page(
                new Query<YxComments>().getPage(params),
                new QueryWrapper<YxComments>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "content", title)
                        .eq(StringUtils.hasText(hotelId), "hotel_id", hotelId)
                        .eq("is_del", 0)
        );
        return new PageUtils(page);
    }


    /**
     * 分页查询用户数据  用户
     *
     * @param params 查询参数
     * @return PageUtils 分页结果
     */
    @Override
    public PageUtils queryUserPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        Long userId = (Long) params.get("userId");
        IPage<YxComments> page = this.page(
                new Query<YxComments>().getPage(params),
                new QueryWrapper<YxComments>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "content", title)
                        .eq("is_del", 0)
                        .eq(userId != null, "user_id", userId)
        );
        return new PageUtils(page);
    }


    /**
     * 保存
     */
    @Override
    public void saveData(YxComments resources) {
        resources.setCreatedTime(new Date());
        resources.setIsDel(0);
        yxCommentsMapper.insert(resources);
    }

    /**
     * 修改
     */
    @Override
    public void updateData(YxComments resources) {
        yxCommentsMapper.updateById(resources);
    }

    /**
     * 删除
     */
    @Override
    public void delData(Long[] ids) {
        for (Long id : ids) {
            YxComments resources = new YxComments();
            resources.setId(id);
            resources.setIsDel(1);
            yxCommentsMapper.updateById(resources);
        }
    }

    /**
     * 用户评论房间信息
     */
    @Override
    public void userCommentsHotel(UserCommentsHotel resources) {

        YxOrder yxOrder = yxOrderMapper.selectById(resources.getOrderId());

        YxHotel yxHotel = yxHotelMapper.selectById(yxOrder.getHomeId());

        SysUser sysUser = sysUserMapper.selectById(SecurityUserIdUtils.getUserId());

        YxComments yxComments = new YxComments();

        yxComments.setHotelId(yxHotel.getId());
        yxComments.setUserId(sysUser.getUserId());
        yxComments.setContent(resources.getContent());
        yxComments.setCreatedTime(new Date());
        yxComments.setIsDel(0);
        yxComments.setHotelName(yxHotel.getHomeName());
        yxComments.setUserName(sysUser.getNickName());
        yxCommentsMapper.insert(yxComments);

    }

}
