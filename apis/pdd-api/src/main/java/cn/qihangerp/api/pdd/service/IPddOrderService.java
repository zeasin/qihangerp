package cn.qihangerp.api.pdd.service;


import cn.qihangerp.api.pdd.domain.PddOrder;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;

import java.util.List;

/**
 * 拼多多订单Service接口
 * 
 * @author qihang
 * @date 2024-01-02
 */
public interface IPddOrderService 
{
    /**
     * 查询拼多多订单
     * 
     * @param id 拼多多订单主键
     * @return 拼多多订单
     */
    public PddOrder selectPddOrderById(Long id);

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
    int confirmOrder(PddOrder pddOrder);

    /**
     * 修改拼多多订单
     * 
     * @param pddOrder 拼多多订单
     * @return 结果
     */
    public int updatePddOrder(PddOrder pddOrder);

    /**
     * 批量删除拼多多订单
     * 
     * @param ids 需要删除的拼多多订单主键集合
     * @return 结果
     */
    public int deletePddOrderByIds(Long[] ids);

    /**
     * 删除拼多多订单信息
     * 
     * @param id 拼多多订单主键
     * @return 结果
     */
    public int deletePddOrderById(Long id);

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
