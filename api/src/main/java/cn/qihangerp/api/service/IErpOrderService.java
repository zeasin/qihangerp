package cn.qihangerp.api.service;

import java.util.List;

import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.ErpSaleOrder;

/**
 * 订单Service接口
 * 
 * @author qihang
 * @date 2024-01-04
 */
public interface IErpOrderService 
{
    /**
     * 查询订单
     * 
     * @param id 订单主键
     * @return 订单
     */
    public ErpSaleOrder selectErpOrderById(Long id);

    /**
     * 查询订单列表
     * 
     * @param erpSaleOrder 订单
     * @return 订单集合
     */
    public List<ErpSaleOrder> selectErpOrderList(ErpSaleOrder erpSaleOrder);

    /**
     * 新增订单
     * 
     * @param erpSaleOrder 订单
     * @return 结果
     */
    public int insertErpOrder(ErpSaleOrder erpSaleOrder);
    // 发货操作移动到erpShipOrder中
//    public int shipErpOrder(ErpOrder erpOrder);


    /**
     * 修改订单
     * 
     * @param erpSaleOrder 订单
     * @return 结果
     */
    public int updateErpOrder(ErpSaleOrder erpSaleOrder);

    /**
     * 批量删除订单
     * 
     * @param ids 需要删除的订单主键集合
     * @return 结果
     */
    public int deleteErpOrderByIds(Long[] ids);

    /**
     * 删除订单信息
     * 
     * @param id 订单主键
     * @return 结果
     */
    public int deleteErpOrderById(Long id);

    ResultVo<Integer> saveOrderMessage(ErpSaleOrder order);
}
