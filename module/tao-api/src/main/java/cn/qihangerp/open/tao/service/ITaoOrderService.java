package cn.qihangerp.open.tao.service;

//import com.qihang.erp.api.common.ResultVo;
//import com.qihang.erp.api.controller.tao.OrderImportItem;
//import com.qihang.erp.api.domain.TaoOrder;

import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import cn.qihangerp.open.tao.domain.TaoOrder;
import cn.qihangerp.open.tao.domain.bo.OrderImportItem;

import java.util.List;

/**
 * 淘宝订单Service接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
public interface ITaoOrderService 
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

    /**
     * 新增淘宝订单
     * 
     * @param taoOrder 淘宝订单
     * @return 结果
     */
    public int insertTaoOrder(TaoOrder taoOrder);
    public int confirmOrder(TaoOrder taoOrder);


    /**
     * 批量删除淘宝订单
     * 
     * @param ids 需要删除的淘宝订单主键集合
     * @return 结果
     */
    public int deleteTaoOrderByIds(Long[] ids);

    /**
     * 删除淘宝订单信息
     * 
     * @param id 淘宝订单主键
     * @return 结果
     */
    public int deleteTaoOrderById(Long id);

    ResultVo<Integer> updateTmallOrderForOpenTaobao(Long shopId, TaoOrder order);
    ResultVo<Integer> excelImportForSubOrder(List<OrderImportItem> orderItemList);

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
