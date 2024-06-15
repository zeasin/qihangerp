package cn.qihangerp.api.mapper;

import cn.qihangerp.domain.ErpSaleOrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author TW
* @description 针对表【erp_order_item(订单明细表)】的数据库操作Mapper
* @createDate 2024-04-15 13:35:02
* @Entity cn.qihangerp.domain.ErpOrderItem
*/
@Mapper
public interface ErpOrderItemMapper extends BaseMapper<ErpSaleOrderItem> {

}




