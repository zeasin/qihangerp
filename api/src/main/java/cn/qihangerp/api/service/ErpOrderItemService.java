package cn.qihangerp.api.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.api.domain.ErpOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【erp_order_item(订单明细表)】的数据库操作Service
* @createDate 2024-04-15 13:35:02
*/
public interface ErpOrderItemService extends IService<ErpOrderItem> {
    PageResult<ErpOrderItem> queryPageList(Integer shipType, Integer status, ErpOrderItem bo, PageQuery pageQuery);
}
