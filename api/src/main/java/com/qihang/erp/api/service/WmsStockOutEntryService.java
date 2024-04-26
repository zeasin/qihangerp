package com.qihang.erp.api.service;

import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.erp.api.domain.ErpOrderItem;
import com.qihang.erp.api.domain.WmsStockOutEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qihang.erp.api.domain.bo.StockOutEntryGenerateBo;

/**
* @author TW
* @description 针对表【wms_stock_out_entry(出库单)】的数据库操作Service
* @createDate 2024-04-26 11:31:15
*/
public interface WmsStockOutEntryService extends IService<WmsStockOutEntry> {
    int generateStockOutEntryForOrderItem(StockOutEntryGenerateBo bo);

    PageResult<WmsStockOutEntry> queryPageList(WmsStockOutEntry bo, PageQuery pageQuery);
}
