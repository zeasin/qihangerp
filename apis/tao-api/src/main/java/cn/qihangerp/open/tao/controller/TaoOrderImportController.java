//package cn.qihangerp.api.controller.tao;
//
//import com.fasterxml.jackson.databind.exc.InvalidFormatException;
//import com.zhijian.common.core.domain.AjaxResult;
//import lombok.extern.java.Log;
//import org.aspectj.weaver.loadtime.Aj;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//@Log
//@RestController
//public class TaoOrderImportController {
//    @RequestMapping(value = "/tao/order/excel_import", method = RequestMethod.POST)
//    public AjaxResult orderImport(@RequestPart("file") MultipartFile file) throws IOException, InvalidFormatException {
//
//        String fileName = file.getOriginalFilename();
//        String dir = System.getProperty("user.dir");
//        String destFileName = dir + File.separator + "/import/uploadedfiles_" + fileName;
//        System.out.println(destFileName);
//        File destFile = new File(destFileName);
//        file.transferTo(destFile);
//        log.info("/***********导入批批网订单，文件后缀" + destFileName.substring(destFileName.lastIndexOf(".")) + "***********/");
//        return AjaxResult.success();
//    }
//}
