package cn.qihangerp.open.tao.mapper;

//import com.qihang.erp.api.domain.TaoOrder;
//import com.qihang.erp.api.domain.TaoOrderItem;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import cn.qihangerp.open.tao.domain.TaoOrder;
import cn.qihangerp.open.tao.domain.TaoOrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 淘宝订单Mapper接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Mapper
public interface TaoOrderMapper 
{
    /**
     * 查询淘宝订单
     * 
     * @param id 淘宝订单主键
     * @return 淘宝订单
     */
    public TaoOrder selectTaoOrderById(String id);

    /**
     * 查询淘宝订单列表
     * 
     * @param taoOrder 淘宝订单
     * @return 淘宝订单集合
     */
    public List<TaoOrder> selectTaoOrderList(TaoOrder taoOrder);
    List<TaoOrderItem> selectOrderItemByOrderId(String orderId);
    TaoOrderItem selectOrderItemBySubItemIdId(Long subItemId);
    /**
     * 新增淘宝订单
     * 
     * @param taoOrder 淘宝订单
     * @return 结果
     */
    public int insertTaoOrder(TaoOrder taoOrder);

    /**
     * 修改淘宝订单
     * 
     * @param taoOrder 淘宝订单
     * @return 结果
     */
    public int updateTaoOrder(TaoOrder taoOrder);
    public int updateTaoOrderItem(TaoOrderItem taoOrderItem);

    /**
     * 删除淘宝订单
     * 
     * @param id 淘宝订单主键
     * @return 结果
     */
    public int deleteTaoOrderById(Long id);

    /**
     * 批量删除淘宝订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaoOrderByIds(Long[] ids);

    /**
     * 批量删除淘宝订单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaoOrderItemByOrderIds(Long[] ids);
    
    /**
     * 批量新增淘宝订单明细
     * 
     * @param taoOrderItemList 淘宝订单明细列表
     * @return 结果
     */
    public int batchTaoOrderItem(List<TaoOrderItem> taoOrderItemList);
    

    /**
     * 通过淘宝订单主键删除淘宝订单明细信息
     * 
     * @param id 淘宝订单ID
     * @return 结果
     */
    public int deleteTaoOrderItemByOrderId(Long id);

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
    ShopSetting selectShopSettingById(Integer id);

    void updateShopSessionByShopId(Long shopId,String sessionKey);
}
