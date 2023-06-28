package com.java.study.modules.sys.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.java.study.common.constant.AppConstants;
import com.java.study.common.exception.RRException;
import com.java.study.common.properties.FileProperties;
import com.java.study.common.utils.FileUtil;
import com.java.study.common.utils.R;
import com.java.study.modules.sys.entity.LocalStorage;
import com.java.study.modules.sys.service.LocalStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件上传
 */
@RestController
@RequestMapping("sys/file")
@Api(tags = {"对象存储/文件上传"})
public class FileController {

    @Autowired
    private FileProperties properties;
    @Autowired
    private LocalStorageService localStorageService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }

        Map<String, Object> map = new LinkedHashMap<>();
        FileUtil.checkSize(properties.getMaxSize(), file.getSize());
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File files = FileUtil.upload(file, properties.getPath().getPath() + File.separator);
        if (ObjectUtil.isNull(files)) {
            throw new RRException("上传失败");
        }
        try {
            String name = IdUtil.simpleUUID();
            LocalStorage localStorage = new LocalStorage();
            localStorage.setRealName(files.getName());
            localStorage.setName(name);
            localStorage.setSuffix(suffix);
            localStorage.setPath(files.getPath());
            localStorage.setType(type);
            localStorage.setSize(FileUtil.getSize(file.getSize()));
            localStorage.setCreateTime(new Date());
            localStorageService.save(localStorage);
            map.put("file", localStorage);
        } catch (Exception e) {
            FileUtil.del(files);
            throw e;
        }
        return R.ok().put("url", AppConstants.HOME_URL + "/wx/files/" + files.getName());

    }
}
