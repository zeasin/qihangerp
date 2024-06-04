package cn.qihangerp.api.wei.service;

import cn.qihangerp.api.wei.domain.OmsWeiGoodsSku;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_wei_goods_sku】的数据库操作Service
* @createDate 2024-05-06 16:01:18
*/
public interface OmsWeiGoodsSkuService extends IService<OmsWeiGoodsSku> {
    PageResult<OmsWeiGoodsSku> queryPageList(OmsWeiGoodsSku bo, PageQuery pageQuery);
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
