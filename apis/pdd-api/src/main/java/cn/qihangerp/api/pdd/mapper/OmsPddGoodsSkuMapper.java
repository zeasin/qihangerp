package cn.qihangerp.api.pdd.mapper;

import cn.qihangerp.api.pdd.domain.OmsPddGoodsSku;
import cn.qihangerp.api.pdd.vo.ErpGoodsSpecVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author TW
* @description 针对表【oms_pdd_goods_sku(pdd商品SKU表)】的数据库操作Mapper
* @createDate 2024-05-28 09:27:08
* @Entity cn.qihangerp.api.pdd.domain.OmsPddGoodsSku
*/
public interface OmsPddGoodsSkuMapper extends BaseMapper<OmsPddGoodsSku> {
    ErpGoodsSpecVo selectGoodsSpecBySpecNum(String specNum);
}




