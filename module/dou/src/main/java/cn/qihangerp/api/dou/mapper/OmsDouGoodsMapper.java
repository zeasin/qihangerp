package cn.qihangerp.api.dou.mapper;

import cn.qihangerp.api.dou.domain.OmsDouGoods;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author TW
* @description 针对表【oms_dou_goods(抖店商品表)】的数据库操作Mapper
* @createDate 2024-05-28 20:35:45
* @Entity cn.qihangerp.api.dou.domain.OmsDouGoods
*/
public interface OmsDouGoodsMapper extends BaseMapper<OmsDouGoods> {
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




