package cn.qihangerp.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.api.domain.WmsStockOutEntryItem;
import cn.qihangerp.api.service.WmsStockOutEntryItemService;
import cn.qihangerp.api.mapper.WmsStockOutEntryItemMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author TW
* @description 针对表【wms_stock_out_entry_item(出库单明细)】的数据库操作Service实现
* @createDate 2024-04-26 13:57:35
*/
@AllArgsConstructor
@Service
public class WmsStockOutEntryItemServiceImpl extends ServiceImpl<WmsStockOutEntryItemMapper, WmsStockOutEntryItem>
    implements WmsStockOutEntryItemService{
    private final WmsStockOutEntryItemMapper mapper;

    @Override
    public PageResult<WmsStockOutEntryItem> queryPageList(WmsStockOutEntryItem bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsStockOutEntryItem> qw = new LambdaQueryWrapper<WmsStockOutEntryItem>();
        qw.eq(WmsStockOutEntryItem::getStockOutType,bo.getStockOutType());
        if(bo.getStatus()!=null) {
            if (bo.getStatus() == 0) {
                qw.and(q -> q.eq(WmsStockOutEntryItem::getStatus, 0).or().eq(WmsStockOutEntryItem::getStatus, 1));
            } else {
                qw.eq(WmsStockOutEntryItem::getStatus, bo.getStatus());
            }
        }
        Page<WmsStockOutEntryItem> pages = mapper.selectPage(pageQuery.build(), qw);

        return PageResult.build(pages);
    }

}




