package cn.qihangerp.api.dou.mapper;

import cn.qihangerp.api.dou.domain.OmsDouGoodsSku;
import cn.qihangerp.api.dou.domain.vo.ErpGoodsSpecVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author TW
* @description 针对表【oms_dou_goods_sku(抖店商品Sku表)】的数据库操作Mapper
* @createDate 2024-05-28 20:35:45
* @Entity cn.qihangerp.api.dou.domain.OmsDouGoodsSku
*/
public interface OmsDouGoodsSkuMapper extends BaseMapper<OmsDouGoodsSku> {
    ErpGoodsSpecVo selectGoodsSpecBySpecNum(String specNum);
}




