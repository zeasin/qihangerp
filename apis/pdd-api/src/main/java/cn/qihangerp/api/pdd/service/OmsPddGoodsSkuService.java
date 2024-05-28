package cn.qihangerp.api.pdd.service;

import cn.qihangerp.api.pdd.domain.OmsPddGoods;
import cn.qihangerp.api.pdd.domain.OmsPddGoodsSku;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_pdd_goods_sku(pdd商品SKU表)】的数据库操作Service
* @createDate 2024-05-28 09:27:08
*/
public interface OmsPddGoodsSkuService extends IService<OmsPddGoodsSku> {
    PageResult<OmsPddGoodsSku> queryPageList(OmsPddGoodsSku bo, PageQuery pageQuery);
}
