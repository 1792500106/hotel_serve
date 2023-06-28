package com.java.study.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.study.modules.sys.entity.LocalStorage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LocalStorageMapper extends BaseMapper<LocalStorage> {
}
