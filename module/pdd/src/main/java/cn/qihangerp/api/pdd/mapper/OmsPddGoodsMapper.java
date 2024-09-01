package cn.qihangerp.api.pdd.mapper;

import cn.qihangerp.api.pdd.domain.OmsPddGoods;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author TW
* @description 针对表【oms_pdd_goods(pdd商品表)】的数据库操作Mapper
* @createDate 2024-05-28 09:27:08
* @Entity cn.qihangerp.api.pdd.domain.OmsPddGoods
*/
public interface OmsPddGoodsMapper extends BaseMapper<OmsPddGoods> {
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

    void updateShopSessionByShopId(@Param("shopId") Long shopId,@Param("accessToken") String accessToken);
}




