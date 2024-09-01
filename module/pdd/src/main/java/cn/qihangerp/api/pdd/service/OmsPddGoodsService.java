package cn.qihangerp.api.pdd.service;

import cn.qihangerp.api.pdd.domain.OmsPddGoods;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_pdd_goods(pdd商品表)】的数据库操作Service
* @createDate 2024-05-28 09:27:08
*/
public interface OmsPddGoodsService extends IService<OmsPddGoods> {
    PageResult<OmsPddGoods> queryPageList(OmsPddGoods bo, PageQuery pageQuery);

    int saveAndUpdateGoods(Long shopId,OmsPddGoods goods);

    Shop selectShopById(Long id);

    /**
     * 查询第三方平台设置
     *
     * @param id 第三方平台设置主键
     * @return 第三方平台设置
     */
    ShopSetting selectShopSettingById(Integer id);
    void updateShopSessionByShopId(Long shopId,String accessToken);
}
