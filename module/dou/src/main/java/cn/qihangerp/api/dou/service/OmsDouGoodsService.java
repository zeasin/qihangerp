package cn.qihangerp.api.dou.service;

import cn.qihangerp.api.dou.domain.OmsDouGoods;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_dou_goods(抖店商品表)】的数据库操作Service
* @createDate 2024-05-28 20:35:45
*/
public interface OmsDouGoodsService extends IService<OmsDouGoods> {

    PageResult<OmsDouGoods> queryPageList(OmsDouGoods bo, PageQuery pageQuery);

    int saveAndUpdateGoods(Long shopId,OmsDouGoods goods);

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
