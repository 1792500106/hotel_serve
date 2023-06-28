package com.java.study.modules.hotel.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.common.exception.RRException;
import com.java.study.common.utils.PageUtils;
import com.java.study.common.utils.Query;
import com.java.study.modules.hotel.dao.YxHotelMapper;
import com.java.study.modules.hotel.dao.YxHotelTypeMapper;
import com.java.study.modules.hotel.entity.YxHotelType;
import com.java.study.modules.hotel.service.YxHotelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Service 实现类层
 */
@Service
public class YxHotelTypeServiceImpl extends ServiceImpl<YxHotelTypeMapper, YxHotelType> implements YxHotelTypeService {

    /**
     * 查询文章列表时返回的字段（过滤掉详情字段以加快速度）
     */
    private static final String LIST_FILEDS = "*";

    @Autowired
    private YxHotelTypeMapper yxHotelTypeMapper;
    @Autowired
    private YxHotelMapper yxHotelMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String) params.get("title");
        IPage<YxHotelType> page = this.page(
                new Query<YxHotelType>().getPage(params),
                new QueryWrapper<YxHotelType>()
                        .select(LIST_FILEDS)
                        .like(StringUtils.hasText(title), "title", title)
                        .eq("is_del", 0)
        );
        return new PageUtils(page);
    }

    /**
     * 保存
     */
    @Override
    public void saveData(YxHotelType resources) {
        resources.setCreatedTime(new Date());
        resources.setIsDel(0);
        yxHotelTypeMapper.insert(resources);
    }

    /**
     * 修改
     */
    @Override
    public void updateData(YxHotelType resources) {
        yxHotelTypeMapper.updateById(resources);
    }

    /**
     * 删除
     */
    @Override
    public void delData(Long[] ids) {
        for (Long id : ids) {
            int countHotel = yxHotelMapper.countHotelByTypeId(id);
            if (countHotel == 0) {
                YxHotelType resources = new YxHotelType();
                resources.setId(id);
                resources.setIsDel(1);
                yxHotelTypeMapper.updateById(resources);
            } else {
                throw new RRException("分类下面有房间，不能删除！");
            }
        }
    }
}
