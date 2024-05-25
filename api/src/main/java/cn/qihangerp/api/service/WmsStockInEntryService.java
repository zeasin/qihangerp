package cn.qihangerp.api.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.api.domain.WmsStockInEntry;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【wms_stock_in_entry(入库单)】的数据库操作Service
* @createDate 2024-04-26 14:54:16
*/
public interface WmsStockInEntryService extends IService<WmsStockInEntry> {
    PageResult<WmsStockInEntry> queryPageList(WmsStockInEntry bo, PageQuery pageQuery);
    int stockIn(WmsStockInEntry wmsStockInEntry);
    int complete(Long id,String updateBy);

    WmsStockInEntry getDetailAndItemById(Long entryId);
}
