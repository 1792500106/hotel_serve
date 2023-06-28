package com.java.study.modules.sys.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.study.modules.banner.entity.YxBanner;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassDesc
 */
@Data
public class UserCountData {

    /**
     * 总用户数
     */
    private Integer sumUserNum = 0;
    /**
     * 总任务数
     */
    private Integer sumTaskNum = 0;

    /**
     * 轮播图
     */
    private List<YxBanner> bannerList;

    private String noticeTile;

    private String noticeContent;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone ="GMT+8")
    private Date noticeTime;


}
