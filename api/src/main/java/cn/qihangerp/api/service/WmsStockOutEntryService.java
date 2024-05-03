package cn.qihangerp.api.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.api.domain.WmsStockOutEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.qihangerp.api.domain.bo.StockOutBo;
import cn.qihangerp.api.domain.bo.StockOutEntryGenerateBo;

/**
* @author TW
* @description 针对表【wms_stock_out_entry(出库单)】的数据库操作Service
* @createDate 2024-04-26 11:31:15
*/
public interface WmsStockOutEntryService extends IService<WmsStockOutEntry> {
    int generateStockOutEntryForOrderItem(StockOutEntryGenerateBo bo);

    PageResult<WmsStockOutEntry> queryPageList(WmsStockOutEntry bo, PageQuery pageQuery);

    int stockOut(StockOutBo bo);
    WmsStockOutEntry selectById(Long id);

    WmsStockOutEntry selectOutEntryItemInventoryDetailsByEntryId(Long entryId);
}
