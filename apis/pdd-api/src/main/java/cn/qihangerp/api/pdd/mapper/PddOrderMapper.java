package cn.qihangerp.api.pdd.mapper;

import cn.qihangerp.api.pdd.domain.PddOrder;
import cn.qihangerp.api.pdd.domain.PddOrderItem;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 拼多多订单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-02
 */
@Mapper
public interface PddOrderMapper 
{
    /**
     * 查询拼多多订单
     * 
     * @param id 拼多多订单主键
     * @return 拼多多订单
     */
    public PddOrder selectPddOrderById(Long id);
    PddOrder selectByOrderSn(String orderSn);

    List<PddOrderItem> selectOrderItemByOrderId(Long orderId);
    PddOrderItem selectOrderItemByOrderItemId(Long orderItemId);

    /**
     * 查询拼多多订单列表
     * 
     * @param pddOrder 拼多多订单
     * @return 拼多多订单集合
     */
    public List<PddOrder> selectPddOrderList(PddOrder pddOrder);

    /**
     * 新增拼多多订单
     * 
     * @param pddOrder 拼多多订单
     * @return 结果
     */
    public int insertPddOrder(PddOrder pddOrder);

    /**
     * 修改拼多多订单
     * 
     * @param pddOrder 拼多多订单
     * @return 结果
     */
    public int updatePddOrder(PddOrder pddOrder);
    public int updatePddOrderItem(PddOrderItem item);

    /**
     * 删除拼多多订单
     * 
     * @param id 拼多多订单主键
     * @return 结果
     */
    public int deletePddOrderById(Long id);

    /**
     * 批量删除拼多多订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePddOrderByIds(Long[] ids);

    /**
     * 批量删除拼多多订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePddOrderItemByOrderIds(Long[] ids);
    
    /**
     * 批量新增拼多多订单明细
     * 
     * @param pddOrderItemList 拼多多订单明细列表
     * @return 结果
     */
    public int batchPddOrderItem(List<PddOrderItem> pddOrderItemList);
    

    /**
     * 通过拼多多订单主键删除拼多多订单明细信息
     * 
     * @param id 拼多多订单ID
     * @return 结果
     */
    public int deletePddOrderItemByOrderId(Long id);

    /**
     * 查询店铺
     *
     * @param id 店铺主键
     * @return 店铺
     */
    Shop selectShopById(Long id);

    /**
     * 查询第三方平台设置
     *
     * @param id 第三方平台设置主键
     * @return 第三方平台设置
     */
    ShopSetting selectShopSettingById(Long id);

    void updateShopSessionByShopId(Long shopId,String sessionKey);
}
