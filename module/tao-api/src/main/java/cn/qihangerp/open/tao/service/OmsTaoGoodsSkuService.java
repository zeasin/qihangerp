package cn.qihangerp.open.tao.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.open.tao.domain.OmsTaoGoods;
import cn.qihangerp.open.tao.domain.OmsTaoGoodsSku;
import cn.qihangerp.open.tao.domain.vo.TaoGoodsSkuListVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_tao_goods_sku(淘宝商品SKU表)】的数据库操作Service
* @createDate 2024-04-29 19:56:59
*/
public interface OmsTaoGoodsSkuService extends IService<OmsTaoGoodsSku> {
    PageResult<TaoGoodsSkuListVo> queryPageList(OmsTaoGoods bo, PageQuery pageQuery);
}
