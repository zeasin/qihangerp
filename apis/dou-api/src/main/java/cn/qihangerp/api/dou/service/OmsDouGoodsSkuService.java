package cn.qihangerp.api.dou.service;

import cn.qihangerp.api.dou.domain.OmsDouGoodsSku;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_dou_goods_sku(抖店商品Sku表)】的数据库操作Service
* @createDate 2024-05-28 20:35:45
*/
public interface OmsDouGoodsSkuService extends IService<OmsDouGoodsSku> {
    PageResult<OmsDouGoodsSku> queryPageList(OmsDouGoodsSku bo, PageQuery pageQuery);
}
