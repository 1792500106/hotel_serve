package com.java.study.modules.sys.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.study.modules.sys.dao.LocalStorageMapper;
import com.java.study.modules.sys.entity.LocalStorage;
import com.java.study.modules.sys.service.LocalStorageService;
import org.springframework.stereotype.Service;

@Service
public class LocalStorageServiceImpl extends ServiceImpl<LocalStorageMapper, LocalStorage> implements LocalStorageService {


}
