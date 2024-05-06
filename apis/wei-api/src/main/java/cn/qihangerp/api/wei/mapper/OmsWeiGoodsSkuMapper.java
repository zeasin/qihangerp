package cn.qihangerp.api.wei.mapper;

import cn.qihangerp.api.wei.domain.OmsWeiGoodsSku;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author TW
* @description 针对表【oms_wei_goods_sku】的数据库操作Mapper
* @createDate 2024-05-06 16:01:18
* @Entity cn.qihangerp.api.wei.domain.OmsWeiGoodsSku
*/
public interface OmsWeiGoodsSkuMapper extends BaseMapper<OmsWeiGoodsSku> {

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

    void updateShopSessionByShopId(@Param("shopId") Long shopId, @Param("sessionKey") String sessionKey);
}




