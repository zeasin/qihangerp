package com.qihang.web.controller.common;

import com.qihang.common.utils.file.MinioHelper;
import com.qihang.domain.SysOss;
import com.qihang.service.ISysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/preview")
public class ImagePreviewController {
    @Autowired
    private MinioHelper minioHelper;
    @Autowired
    private ISysOssService sysOssService;
    @GetMapping("/images/{ossId}")
    public void previewImage(@PathVariable Long ossId, HttpServletResponse response) {
        SysOss sysOss = sysOssService.selectSysOssByOssId(ossId);
        if(sysOss== null) return;
        minioHelper.previewImage(sysOss.getObjectName(),response);
    }
}
