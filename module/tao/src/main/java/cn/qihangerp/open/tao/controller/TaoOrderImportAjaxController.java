//package cn.qihangerp.api.controller.tao;
//
//import com.fasterxml.jackson.databind.exc.InvalidFormatException;
//import cn.qihangerp.api.common.ApiResult;
//import cn.qihangerp.api.common.EnumTmallOrderStatus;
//import cn.qihangerp.api.service.ITaoOrderService;
//import cn.qihangerp.api.service.impl.TaoOrderServiceImpl;
//import cn.qihangerp.api.utils.DateUtil;
//import lombok.AllArgsConstructor;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.util.NumberToTextConverter;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@AllArgsConstructor
//@RestController
//public class TaoOrderImportAjaxController {
//    private final ITaoOrderService taoOrderService;
////    @Autowired
////    private ErpGoodsService erpGoodsService;
////
////    @Autowired
////    private DcTmallOrderService tmallOrderService;
//
//    private static Logger log = LoggerFactory.getLogger(TaoOrderImportAjaxController.class);
//
//    /***
//     * 从菜单打印订单导出excel中批量发货
//     * @param file
//     * @param shopId
//     * @return
//     * @throws IOException
//     * @throws InvalidFormatException
//     */
//    @RequestMapping(value = "/tao/order/order_import", method = RequestMethod.POST)
//    public ApiResult<List<OrderImportEntity>> orderSendExcel(@RequestPart("file") MultipartFile file, @RequestParam Integer shopId) throws IOException, InvalidFormatException {
//
//        String fileName = file.getOriginalFilename();
//        String dir = System.getProperty("user.dir");
//        String destFileName = dir + File.separator + "/import/uploadedfiles_" + fileName;
//        System.out.println(destFileName);
//        File destFile = new File(destFileName);
//        file.transferTo(destFile);
//        log.info("/***********导入批批网订单，文件后缀" + destFileName.substring(destFileName.lastIndexOf(".")) + "***********/");
//        InputStream fis = null;
//        fis = new FileInputStream(destFileName);
//        if (fis == null) return new ApiResult<>(502, "没有文件");
//
//        Workbook workbook = null;
//
//        try {
//            if (fileName.toLowerCase().endsWith("xlsx")) {
//                workbook = new XSSFWorkbook(fis);
//            } else if (fileName.toLowerCase().endsWith("xls")) {
//                workbook = new HSSFWorkbook(fis);
//            }
//            // workbook = new HSSFWorkbook(fis);
//        } catch (Exception ex) {
//            log.info("/***********导入批批网订单***出现异常：" + ex.getMessage() + "***********/");
//            return new ApiResult<>(500, ex.getMessage());
//        }
//
//        if (workbook == null) return new ApiResult<>(502, "未读取到Excel文件");
//
//
//        /****************开始处理批批网csv订单****************/
//        //订单list
//        List<OrderImportEntity> orderList = new ArrayList<>();
//        Sheet sheet = null;
//
//        try {
//            sheet = workbook.getSheetAt(0);
//            int lastRowNum = sheet.getLastRowNum();//最后一行索引
//            Row row = null;
//
//            for (int i = 1; i <= lastRowNum; i++) {
//                row = sheet.getRow(i);
//                //订单数据
//                String orderNum = row.getCell(0).getStringCellValue().replace("\t", "");
//
//                log.info("/***********导入批批网订单***读取到订单编号:" + orderNum + "***********/");
//
//                if (StringUtils.isEmpty(orderNum) == false) {
//                    //订单实体
//                    OrderImportEntity order = new OrderImportEntity();
//                    Cell cell = row.getCell(0);
//                    String cellValue = "";
//                    if (cell != null) {
//                        if (cell.getCellType() == CellType.STRING) {
//                            cellValue = cell.getStringCellValue().replace("\t", "");
//                        } else if (cell.getCellType() == CellType.NUMERIC) {
//                            cellValue = NumberToTextConverter.toText(cell.getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//
//                        order.setOrderNum(orderNum);
//                        order.setBuyerName("");
//                        Double goodsAmount = Double.parseDouble(row.getCell(3).getStringCellValue().replace("\t", ""));
//                        Double expressFee = Double.parseDouble(row.getCell(4).getStringCellValue().replace("\t", ""));
//                        Double amount = Double.parseDouble(row.getCell(6).getStringCellValue().replace("\t", ""));//总金额
//                        Double payAmount = Double.parseDouble(row.getCell(8).getStringCellValue().replace("\t", ""));//实际支付
//                        order.setGoodsAmount(goodsAmount);
//                        order.setExpressFee(expressFee);
//                        order.setOrderAmount(amount);
//                        order.setPayAmount(payAmount);
//                        String statusStr = row.getCell(10).getStringCellValue().replace("\t", "");
//                        // EnumTmallOrderStatus
//                        //订单状态
//                        order.setStatusStr(statusStr);
//                        if (statusStr.trim().equals("买家已付款，等待卖家发货")) {
//                            order.setStatus(EnumTmallOrderStatus.WAIT_SEND_GOODS.getStatus());
//                        }
//
//                        String buyerFeedback = row.getCell(11).getStringCellValue().replace("\t", "").replace("null", "");
//                        order.setBuyerFeedback(buyerFeedback);
//
//                        //收货人
//                        String receiver = row.getCell(12).getStringCellValue().replace("\t", "");
//                        String mobile = row.getCell(16).getStringCellValue().replace("\t", "").replace("'","");
//                        String address = row.getCell(13).getStringCellValue().replace("\t", "");
//                        order.setContactPerson(receiver);
//                        order.setContactMobile(mobile);
//                        try {
//                            order.setProvince(address.split(" ")[0]);
//                            order.setCity(address.split(" ")[1]);
//                            order.setArea(address.split(" ")[2].replace("null", ""));
//                        } catch (Exception e) {
//                        }
//                        order.setAddress(address);
//
//                        String dateFormat = "yyyy-MM-dd HH:mm:ss";
//                        String orderTime = row.getCell(18).getStringCellValue().replace("\t", "");
//                        String payTime = row.getCell(19).getStringCellValue().replace("\t", "");
//                        //订单时间
//                        order.setOrderTime(DateUtil.dateToStamp(DateUtil.stringtoDate(orderTime, dateFormat)));
//                        order.setOrderTimeStr(orderTime);
//                        order.setPayTime(payTime);
//
//                        Integer goodsCount = Integer.parseInt(row.getCell(25).getStringCellValue().replace("\t", ""));
//                        order.setGoodsCount(goodsCount);
//
//                        String logisticsCode = row.getCell(22).getStringCellValue().replace("\t", "");
//                        String logisticsCompany = row.getCell(23).getStringCellValue().replace("\t", "").replace("null", "");
//                        order.setLogisticsCode(logisticsCode);
//                        order.setLogisticsCompany(logisticsCompany);
//
//                        String sellerMemo = row.getCell(24).getStringCellValue().replace("\t", "").replace("null", "").replace("'", "");
//                        order.setSellerMemo(sellerMemo);
//                        String shopName = row.getCell(27).getStringCellValue().replace("\t", "");
//                        order.setShopId(shopId);
//
//                        //添加订单到list
//                        orderList.add(order);
//                }
//
//
//
//
//
//            }
//
//
//        } catch (Exception ex) {
//            log.info("/***********导入批批网订单***出现异常：" + ex.getMessage() + "***********/");
//            return new ApiResult<>(500, ex.getMessage());
//        }
//
//
//        int orderCount = orderList.size();
//        log.info("/***********导入批批网订单***读取到：" + orderCount + "个订单***********/");
//
//        return new ApiResult<>(0, "SUCCESS", orderList);
//    }
//
//
//    /**
//     * 补充orderItem
//     * @param file
//     * @param req
//     * @return
//     * @throws IOException
//     * @throws InvalidFormatException
//     */
//    @RequestMapping(value = "/tao/order/order_item_import", method = RequestMethod.POST)
//    public ApiResult<List<OrderImportItem>> orderItemExcel(@RequestPart("file") MultipartFile file, @RequestParam Integer shopId) throws IOException, InvalidFormatException {
//
//        String fileName = file.getOriginalFilename();
//        String dir = System.getProperty("user.dir");
//        String destFileName = dir + File.separator + "/import/uploadedfiles_" + fileName;
//        System.out.println(destFileName);
//        File destFile = new File(destFileName);
//        file.transferTo(destFile);
//        log.info("/***********导入TAO订单，文件后缀" + destFileName.substring(destFileName.lastIndexOf(".")) + "***********/");
//        InputStream fis = null;
//        fis = new FileInputStream(destFileName);
//        if (fis == null) return new ApiResult<>(502, "没有文件");
//
//        Workbook workbook = null;
//
//        try {
//            if (fileName.toLowerCase().endsWith("xlsx")) {
//                workbook = new XSSFWorkbook(fis);
//            } else if (fileName.toLowerCase().endsWith("xls")) {
//                workbook = new HSSFWorkbook(fis);
//            }
//            // workbook = new HSSFWorkbook(fis);
//        } catch (Exception ex) {
//            log.info("/***********导入TAO订单***出现异常：" + ex.getMessage() + "***********/");
//            return new ApiResult<>(500, ex.getMessage());
//        }
//
//        if (workbook == null) return new ApiResult<>(502, "未读取到Excel文件");
//
//
//        /****************开始处理excel订单数据****************/
//        //订单list
//        List<OrderImportItem> orderItemList = new ArrayList<>();
//        Sheet sheet = null;
//
//        try {
//            sheet = workbook.getSheetAt(0);
//            int lastRowNum = sheet.getLastRowNum();//最后一行索引
//            Row row = null;
//
//            for (int i = 1; i <= lastRowNum; i++) {
//                row = sheet.getRow(i);
//
//                //订单数据
////                String orderNum = row.getCell(1).getStringCellValue().replace("\t", "");
//                String orderNum = "";
//                if (row.getCell(1) != null) {
//                    if (row.getCell(1).getCellType() == CellType.STRING) {
//                        orderNum = row.getCell(1).getStringCellValue().replace("\t", "");
//                    } else if (row.getCell(1).getCellType() == CellType.NUMERIC) {
//                        orderNum = NumberToTextConverter.toText(row.getCell(1).getNumericCellValue()).replace("\t", "");
//                    }
//                }
//                log.info("/***********导入TAO订单***读取到订单编号:" + orderNum + "***********/");
//
//                if (StringUtils.isEmpty(orderNum) == false) {
//                    OrderImportItem oItem = new OrderImportItem();
//                    oItem.setShopId(shopId);
//                    oItem.setOrderNum(orderNum);
//                    String subOrderNum ="";
//                    if (row.getCell(0) != null) {
//                        if (row.getCell(0).getCellType() == CellType.STRING) {
//                            subOrderNum = row.getCell(0).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(0).getCellType() == CellType.NUMERIC) {
//                            subOrderNum = NumberToTextConverter.toText(row.getCell(0).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setSubOrderNum(subOrderNum);
//
//                    oItem.setGoodsTitle(row.getCell(2).getStringCellValue().replace("\t", ""));
//                    String price ="";
//                    if (row.getCell(3) != null) {
//                        if (row.getCell(3).getCellType() == CellType.STRING) {
//                            price = row.getCell(3).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(3).getCellType() == CellType.NUMERIC) {
//                            price = NumberToTextConverter.toText(row.getCell(3).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setPrice(new BigDecimal(price));
//                    String quantity ="";
//                    if (row.getCell(4) != null) {
//                        if (row.getCell(4).getCellType() == CellType.STRING) {
//                            quantity = row.getCell(4).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(4).getCellType() == CellType.NUMERIC) {
//                            quantity = NumberToTextConverter.toText(row.getCell(4).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setQuantity(Long.parseLong(quantity));
//                    String goodsNum ="";
//                    if (row.getCell(5) != null) {
//                        if (row.getCell(5).getCellType() == CellType.STRING) {
//                            goodsNum = row.getCell(5).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(5).getCellType() == CellType.NUMERIC) {
//                            goodsNum = NumberToTextConverter.toText(row.getCell(5).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setGoodsNumber(goodsNum);
//                    String skuNum ="";
//                    if (row.getCell(10) != null) {
//                        if (row.getCell(10).getCellType() == CellType.STRING) {
//                            skuNum = row.getCell(10).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(10).getCellType() == CellType.NUMERIC) {
//                            skuNum = NumberToTextConverter.toText(row.getCell(10).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setSpecNumber(skuNum);
//
//                    oItem.setSkuInfo(row.getCell(6).getStringCellValue().replace("\t", ""));
//                    oItem.setStatus(row.getCell(9).getStringCellValue().replace("\t", ""));
//
//                    String amount ="";
//                    if (row.getCell(12) != null) {
//                        if (row.getCell(12).getCellType() == CellType.STRING) {
//                            amount = row.getCell(12).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(12).getCellType() == CellType.NUMERIC) {
//                            amount = NumberToTextConverter.toText(row.getCell(12).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setAmount(new BigDecimal(amount));
//                    String payAmount ="";
//                    if (row.getCell(13) != null) {
//                        if (row.getCell(13).getCellType() == CellType.STRING) {
//                            payAmount = row.getCell(13).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(13).getCellType() == CellType.NUMERIC) {
//                            payAmount = NumberToTextConverter.toText(row.getCell(13).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setPayAmount(new BigDecimal(payAmount));
//                    oItem.setRefundStatus(row.getCell(14).getStringCellValue().replace("\t", ""));
//                    String refundAmount ="";
//                    if (row.getCell(15) != null) {
//                        if (row.getCell(15).getCellType() == CellType.STRING) {
//                            refundAmount = row.getCell(15).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(15).getCellType() == CellType.NUMERIC) {
//                            refundAmount = NumberToTextConverter.toText(row.getCell(15).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setRefundAmount(refundAmount);
//                    oItem.setOrderCreated(row.getCell(16).getStringCellValue().replace("\t", ""));
//                    oItem.setOrderPayTime(row.getCell(17).getStringCellValue().replace("\t", ""));
//                    String numIid ="";
//                    if (row.getCell(18) != null) {
//                        if (row.getCell(18).getCellType() == CellType.STRING) {
//                            numIid = row.getCell(18).getStringCellValue().replace("\t", "");
//                        } else if (row.getCell(18).getCellType() == CellType.NUMERIC) {
//                            numIid = NumberToTextConverter.toText(row.getCell(18).getNumericCellValue()).replace("\t", "");
//                        }
//                    }
//                    oItem.setNumIid(numIid);
//                    oItem.setSellerMemo(row.getCell(21).getStringCellValue().replace("\t", ""));
//                    oItem.setBuyerMemo(row.getCell(22).getStringCellValue().replace("\t", ""));
//                    oItem.setSendTime(row.getCell(23).getStringCellValue().replace("\t", ""));
//                    oItem.setLogisticsCode(row.getCell(24).getStringCellValue().replace("\t", ""));
//                    oItem.setLogisticsCom(row.getCell(25).getStringCellValue().replace("\t", ""));
//                    orderItemList.add(oItem);
//                }
//            }
//        } catch (Exception ex) {
//            log.info("/***********导入TAO订单***出现异常：" + ex.getMessage() + "***********/");
//            return new ApiResult<>(500, "excel格式不正确");
//        }
//
//        //添加进去
//        taoOrderService.excelImportForSubOrder(orderItemList);
//        int orderCount = orderItemList.size();
//        log.info("/***********导入TAO订单***读取到：" + orderCount + "个订单***********/");
//
//        return new ApiResult<>(0, "SUCCESS", orderItemList);
//    }
//
//    /***
//     * 导入批批网订单
//     * @param req
//     * @return
//     */
////    @RequestMapping(value = "/order_excel_import_review_submit", method = RequestMethod.POST)
////    public ApiResult<String> orderExcelImportSubmit(@RequestBody TaoOrderImportSubmitReq req) {
//////        if (req.getBuyerUserId() == null || req.getBuyerUserId() == 0)
//////            return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误：没有buyerUserId");
////
////        List<OrderImportPiPiEntity> orderList = req.getOrderList();
////        if (orderList == null || orderList.size() == 0)
////            return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误：没有orderList");
////        Integer shopId = req.getShopId();
////        /*************excel订单货号检查，1是否填写了，2是否存在 ********** **************/
////        for (var order : orderList) {
////            for (var item : order.getItems()) {
////                if (StringUtils.isEmpty(item.getGoodsNumber())) {
////                    return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误：订单" + order.getOrderNum() + "没有货号");
////                } else {
////                    //查询商品信息
////                    // var erpGoods = erpGoodsService.getGoodsEntityByNumber(item.getGoodsNumber());
////                    var erpGoodsSpec = erpGoodsService.getSpecByNumber(item.getGoodsNumber());
////                    if (erpGoodsSpec == null) {
////                        return new ApiResult<>(EnumResultVo.ParamsError.getIndex(), "参数错误：订单" + order.getOrderNum() + "货号:" + item.getGoodsNumber() + "不存在");
////                    } else {
////                        //重新赋值
////                        item.setGoodsId(erpGoodsSpec.getGoodsId());
////                        item.setGoodsTitle(erpGoodsSpec.getGoodTitle());
////                        item.setGoodsImg(erpGoodsSpec.getColorImage());
////                        item.setSpecId(erpGoodsSpec.getId());
////                        item.setGoodsNumber(erpGoodsSpec.getGoodsNumber());
////                        item.setSpecNumber(erpGoodsSpec.getSpecNumber());
//////                        item.setPrice(erpGoods.getWholesalePrice());
////                    }
////                }
////            }
////        }
////
////        ResultVo<String> resultVo = tmallOrderService.importExcelOrder(orderList,shopId);
////
////        if (resultVo.getCode() == EnumResultVo.SUCCESS.getIndex()) {
////            return new ApiResult<>(0, "SUCCESS", resultVo.getData());
////        } else return new ApiResult<>(resultVo.getCode(), resultVo.getMsg());
////    }
//
//}
