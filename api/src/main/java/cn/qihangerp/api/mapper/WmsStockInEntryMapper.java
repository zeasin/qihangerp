package cn.qihangerp.api.mapper;

import cn.qihangerp.api.domain.WmsStockInEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author TW
* @description 针对表【wms_stock_in_entry(入库单)】的数据库操作Mapper
* @createDate 2024-04-26 14:54:16
* @Entity cn.qihangerp.api.domain.WmsStockInEntry
*/
@Mapper
public interface WmsStockInEntryMapper extends BaseMapper<WmsStockInEntry> {

}




