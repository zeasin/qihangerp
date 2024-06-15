package cn.qihangerp.api.mapper;

import java.util.List;
import cn.qihangerp.domain.ErpSaleOrder;
import cn.qihangerp.domain.ErpSaleOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-05
 */
@Mapper
public interface ErpOrderMapper 
{
    /**
     * 查询订单
     * 
     * @param id 订单主键
     * @return 订单
     */
    public ErpSaleOrder selectErpOrderById(Long id);
    ErpSaleOrder selectErpOrderByNum(String orderNum);

    /**
     * 查询订单列表
     * 
     * @param erpSaleOrder 订单
     * @return 订单集合
     */
    public List<ErpSaleOrder> selectErpOrderList(ErpSaleOrder erpSaleOrder);

    List<ErpSaleOrderItem> selectOrderItemByOrderId(Long orderId);

    ErpSaleOrderItem selectOrderItemByOrderItemNum(ErpSaleOrderItem orderItem);

    /**
     * 新增订单
     * 
     * @param erpSaleOrder 订单
     * @return 结果
     */
    public int insertErpOrder(ErpSaleOrder erpSaleOrder);

    /**
     * 修改订单
     * 
     * @param erpSaleOrder 订单
     * @return 结果
     */
    public int updateErpOrder(ErpSaleOrder erpSaleOrder);

    /**
     * 删除订单
     * 
     * @param id 订单主键
     * @return 结果
     */
    public int deleteErpOrderById(Long id);

    /**
     * 批量删除订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErpOrderByIds(Long[] ids);

    /**
     * 批量删除订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErpOrderItemByOrderIds(Long[] ids);
    
    /**
     * 批量新增订单明细
     * 
     * @param erpSaleOrderItemList 订单明细列表
     * @return 结果
     */
    public int batchErpOrderItem(List<ErpSaleOrderItem> erpSaleOrderItemList);
    public int insertErpOrderItem(ErpSaleOrderItem erpSaleOrderItem);


    /**
     * 通过订单主键删除订单明细信息
     * 
     * @param id 订单ID
     * @return 结果
     */
    public int deleteErpOrderItemByOrderId(Long id);
}
