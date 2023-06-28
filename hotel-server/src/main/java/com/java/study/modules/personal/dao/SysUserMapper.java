package com.java.study.modules.personal.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.personal.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
