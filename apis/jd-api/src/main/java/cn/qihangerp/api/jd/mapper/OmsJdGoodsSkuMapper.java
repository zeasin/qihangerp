package cn.qihangerp.api.jd.mapper;

import cn.qihangerp.api.jd.domain.OmsJdGoodsSku;
import cn.qihangerp.api.jd.domain.vo.ErpGoodsSpecVo;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author qilip
* @description 针对表【oms_jd_goods_sku(京东商品SKU表)】的数据库操作Mapper
* @createDate 2024-05-03 18:09:33
* @Entity cn.qihangerp.api.jd.domain.OmsJdGoodsSku
*/
public interface OmsJdGoodsSkuMapper extends BaseMapper<OmsJdGoodsSku> {
    ErpGoodsSpecVo selectGoodsSpecBySpecNum(String specNum);

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




