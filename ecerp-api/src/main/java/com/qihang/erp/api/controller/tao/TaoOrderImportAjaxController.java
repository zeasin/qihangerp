package com.qihang.erp.api.controller.tao;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.qihang.erp.api.common.ApiResult;
import com.qihang.erp.api.common.EnumTmallOrderStatus;
import com.qihang.erp.api.utils.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RestController
public class TaoOrderImportAjaxController {
//    @Autowired
//    private ErpGoodsService erpGoodsService;
//
//    @Autowired
//    private DcTmallOrderService tmallOrderService;

    private static Logger log = LoggerFactory.getLogger(TaoOrderImportAjaxController.class);

    /***
     * 从菜单打印订单导出excel中批量发货
     * @param file
     * @param req
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = "/tao/order/order_import", method = RequestMethod.POST)
    public ApiResult<List<OrderImportEntity>> orderSendExcel(@RequestPart("file") MultipartFile file, HttpServletRequest req) throws IOException, InvalidFormatException {

        String fileName = file.getOriginalFilename();
        String dir = System.getProperty("user.dir");
        String destFileName = dir + File.separator + "/import/uploadedfiles_" + fileName;
        System.out.println(destFileName);
        File destFile = new File(destFileName);
        file.transferTo(destFile);
        log.info("/***********导入批批网订单，文件后缀" + destFileName.substring(destFileName.lastIndexOf(".")) + "***********/");
        InputStream fis = null;
        fis = new FileInputStream(destFileName);
        if (fis == null) return new ApiResult<>(502, "没有文件");

        Workbook workbook = null;
        
        try {
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }
            // workbook = new HSSFWorkbook(fis);
        } catch (Exception ex) {
            log.info("/***********导入批批网订单***出现异常：" + ex.getMessage() + "***********/");
            return new ApiResult<>(500, ex.getMessage());
        }

        if (workbook == null) return new ApiResult<>(502, "未读取到Excel文件");


        /****************开始处理批批网csv订单****************/
        //订单list
        List<OrderImportEntity> orderList = new ArrayList<>();
        Sheet sheet = null;

        try {
            sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();//最后一行索引
            Row row = null;

            for (int i = 1; i <= lastRowNum; i++) {
                row = sheet.getRow(i);
                //订单数据
                String orderNum = row.getCell(0).getStringCellValue().replace("\t", "");

                log.info("/***********导入批批网订单***读取到订单编号:" + orderNum + "***********/");

                if (StringUtils.isEmpty(orderNum) == false) {
                    //订单实体
                    OrderImportEntity order = new OrderImportEntity();

               
                        order.setOrderNum(orderNum);
                        order.setBuyerName("");
                        Double goodsAmount = Double.parseDouble(row.getCell(3).getStringCellValue().replace("\t", ""));
                        Double expressFee = Double.parseDouble(row.getCell(4).getStringCellValue().replace("\t", ""));
                        Double amount = Double.parseDouble(row.getCell(6).getStringCellValue().replace("\t", ""));//总金额
                        Double payAmount = Double.parseDouble(row.getCell(8).getStringCellValue().replace("\t", ""));//实际支付
                        order.setGoodsAmount(goodsAmount);
                        order.setExpressFee(expressFee);
                        order.setOrderAmount(amount);
                        order.setPayAmount(payAmount);
                        String statusStr = row.getCell(10).getStringCellValue().replace("\t", "");
                        // EnumTmallOrderStatus
                        //订单状态
                        order.setStatusStr(statusStr);
                        if (statusStr.trim().equals("买家已付款，等待卖家发货")) {
                            order.setStatus(EnumTmallOrderStatus.WAIT_SEND_GOODS.getStatus());
                        }

                        String buyerFeedback = row.getCell(11).getStringCellValue().replace("\t", "").replace("null", "");
                        order.setBuyerFeedback(buyerFeedback);

                        //收货人
                        String receiver = row.getCell(12).getStringCellValue().replace("\t", "");
                        String mobile = row.getCell(16).getStringCellValue().replace("\t", "").replace("'","");
                        String address = row.getCell(13).getStringCellValue().replace("\t", "");
                        order.setContactPerson(receiver);
                        order.setContactMobile(mobile);
                        try {
                            order.setProvince(address.split(" ")[0]);
                            order.setCity(address.split(" ")[1]);
                            order.setArea(address.split(" ")[2].replace("null", ""));
                        } catch (Exception e) {
                        }
                        order.setAddress(address);

                        String dateFormat = "yyyy-MM-dd HH:mm:ss";
                        String orderTime = row.getCell(18).getStringCellValue().replace("\t", "");
                        String payTime = row.getCell(19).getStringCellValue().replace("\t", "");
                        //订单时间
                        order.setOrderTime(DateUtil.dateToStamp(DateUtil.stringtoDate(orderTime, dateFormat)));
                        order.setOrderTimeStr(orderTime);
                        order.setPayTime(payTime);
                       
                        Integer goodsCount = Integer.parseInt(row.getCell(25).getStringCellValue().replace("\t", ""));
                        order.setGoodsCount(goodsCount);

                        String logisticsCode = row.getCell(22).getStringCellValue().replace("\t", "");
                        String logisticsCompany = row.getCell(23).getStringCellValue().replace("\t", "").replace("null", "");
                        order.setLogisticsCode(logisticsCode);
                        order.setLogisticsCompany(logisticsCompany);

                        String sellerMemo = row.getCell(24).getStringCellValue().replace("\t", "").replace("null", "").replace("'", "");
                        order.setSellerMemo(sellerMemo);
                        String shopName = row.getCell(27).getStringCellValue().replace("\t", "");
                        if(shopName.equals("珍姐姐de衣柜")){
                            Integer shopId = 6;
                            order.setShopId(shopId);
                        }
                        

                        //添加订单到list
                        orderList.add(order);
                }


                


            }


        } catch (Exception ex) {
            log.info("/***********导入批批网订单***出现异常：" + ex.getMessage() + "***********/");
            return new ApiResult<>(500, ex.getMessage());
        }


        int orderCount = orderList.size();
        log.info("/***********导入批批网订单***读取到：" + orderCount + "个订单***********/");

        return new ApiResult<>(0, "SUCCESS", orderList);
    }


    /**
     * 补充orderItem
     * @param file
     * @param req
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */    
    @RequestMapping(value = "/tao/order/order_item_import", method = RequestMethod.POST)
    public ApiResult<List<OrderImportItem>> orderItemExcel(@RequestPart("file") MultipartFile file, HttpServletRequest req) throws IOException, InvalidFormatException {

        String fileName = file.getOriginalFilename();
        String dir = System.getProperty("user.dir");
        String destFileName = dir + File.separator + "/import/uploadedfiles_" + fileName;
        System.out.println(destFileName);
        File destFile = new File(destFileName);
        file.transferTo(destFile);
        log.info("/***********导入批批网订单，文件后缀" + destFileName.substring(destFileName.lastIndexOf(".")) + "***********/");
        InputStream fis = null;
        fis = new FileInputStream(destFileName);
        if (fis == null) return new ApiResult<>(502, "没有文件");

        Workbook workbook = null;
        
        try {
            if (fileName.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileName.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(fis);
            }
            // workbook = new HSSFWorkbook(fis);
        } catch (Exception ex) {
            log.info("/***********导入批批网订单***出现异常：" + ex.getMessage() + "***********/");
            return new ApiResult<>(500, ex.getMessage());
        }

        if (workbook == null) return new ApiResult<>(502, "未读取到Excel文件");


        /****************开始处理批批网csv订单****************/
        //订单list
        List<OrderImportItem> orderItemList = new ArrayList<>();
        Sheet sheet = null;

        try {
            sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();//最后一行索引
            Row row = null;

            for (int i = 1; i <= lastRowNum; i++) {
                row = sheet.getRow(i);
                //订单数据
                String orderNum = row.getCell(1).getStringCellValue().replace("\t", "");

                log.info("/***********导入批批网订单***读取到订单编号:" + orderNum + "***********/");

                if (StringUtils.isEmpty(orderNum) == false) {
                    OrderImportItem oItem = new OrderImportItem();

                    oItem.setOrderNum(orderNum);
                    oItem.setSubOrderNum(row.getCell(0).getStringCellValue().replace("\t", ""));
                    oItem.setGoodsTitle(row.getCell(2).getStringCellValue().replace("\t", ""));
                    oItem.setPrice(new BigDecimal(row.getCell(3).getNumericCellValue()));
                    oItem.setQuantity(new BigDecimal(row.getCell(4).getNumericCellValue()).longValue());
                    try {
                        oItem.setGoodsNumber(row.getCell(5).getStringCellValue().replace("\t", ""));
                    } catch (Exception e) {
                        oItem.setGoodsNumber(row.getCell(5).getNumericCellValue()+"");
                    }
                    
                    oItem.setSkuInfo(row.getCell(6).getStringCellValue().replace("\t", ""));
                    oItem.setPayAmount(new BigDecimal(row.getCell(13).getNumericCellValue()));
                    orderItemList.add(oItem);

                }


            }


        } catch (Exception ex) {
            log.info("/***********导入批批网订单***出现异常：" + ex.getMessage() + "***********/");
            return new ApiResult<>(500, ex.getMessage());
        }


        int orderCount = orderItemList.size();
        log.info("/***********导入批批网订单***读取到：" + orderCount + "个订单***********/");

        return new ApiResult<>(0, "SUCCESS", orderItemList);
    }

    /***
     * 导入批批网订单
     * @param req
     * @return
     */
//    @RequestMapping(value = "/order_excel_import_review_submit", method = RequestMethod.POST)
//    public ApiResult<String> orderExcelImportSubmit(@RequestBody TaoOrderImportSubmitReq req) {
////        if (req.getBuyerUserId() == null || req.getBuyerUserId() == 0)
////            return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误：没有buyerUserId");
//
//        List<OrderImportPiPiEntity> orderList = req.getOrderList();
//        if (orderList == null || orderList.size() == 0)
//            return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误：没有orderList");
//        Integer shopId = req.getShopId();
//        /*************excel订单货号检查，1是否填写了，2是否存在 ********** **************/
//        for (var order : orderList) {
//            for (var item : order.getItems()) {
//                if (StringUtils.isEmpty(item.getGoodsNumber())) {
//                    return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误：订单" + order.getOrderNum() + "没有货号");
//                } else {
//                    //查询商品信息
//                    // var erpGoods = erpGoodsService.getGoodsEntityByNumber(item.getGoodsNumber());
//                    var erpGoodsSpec = erpGoodsService.getSpecByNumber(item.getGoodsNumber());
//                    if (erpGoodsSpec == null) {
//                        return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误：订单" + order.getOrderNum() + "货号:" + item.getGoodsNumber() + "不存在");
//                    } else {
//                        //重新赋值
//                        item.setGoodsId(erpGoodsSpec.getGoodsId());
//                        item.setGoodsTitle(erpGoodsSpec.getGoodTitle());
//                        item.setGoodsImg(erpGoodsSpec.getColorImage());
//                        item.setSpecId(erpGoodsSpec.getId());
//                        item.setGoodsNumber(erpGoodsSpec.getGoodsNumber());
//                        item.setSpecNumber(erpGoodsSpec.getSpecNumber());
////                        item.setPrice(erpGoods.getWholesalePrice());
//                    }
//                }
//            }
//        }
//
//        ResultVo<String> resultVo = tmallOrderService.importExcelOrder(orderList,shopId);
//
//        if (resultVo.getCode() == EnumResultVo.SUCCESS.getIndex()) {
//            return new ApiResult<>(0, "SUCCESS", resultVo.getData());
//        } else return new ApiResult<>(resultVo.getCode(), resultVo.getMsg());
//    }

}
