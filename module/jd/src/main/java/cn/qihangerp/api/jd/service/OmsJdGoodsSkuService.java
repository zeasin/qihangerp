package cn.qihangerp.api.jd.service;

import cn.qihangerp.api.jd.domain.OmsJdGoodsSku;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【oms_jd_goods_sku(京东商品SKU表)】的数据库操作Service
* @createDate 2024-05-03 18:09:33
*/
public interface OmsJdGoodsSkuService extends IService<OmsJdGoodsSku> {
    PageResult<OmsJdGoodsSku> queryPageList(OmsJdGoodsSku bo, PageQuery pageQuery);

    ResultVo<Integer> saveGoodsSku(Long shopId, OmsJdGoodsSku goodsSku);
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
