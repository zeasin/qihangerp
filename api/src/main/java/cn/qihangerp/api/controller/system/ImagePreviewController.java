package cn.qihangerp.api.controller.system;

import cn.qihangerp.api.domain.SysOss;
import cn.qihangerp.api.service.ISysOssService;
import cn.qihangerp.common.utils.file.MinioHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

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
