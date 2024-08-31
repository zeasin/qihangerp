package cn.qihangerp.open.tao.mapper;

import cn.qihangerp.open.tao.domain.OmsTaoGoodsSku;
import cn.qihangerp.open.tao.domain.vo.ErpGoodsSpecVo;
import cn.qihangerp.open.tao.domain.vo.TaoGoodsSkuListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* @author TW
* @description 针对表【oms_tao_goods_sku(淘宝商品SKU表)】的数据库操作Mapper
* @createDate 2024-04-29 19:56:59
* @Entity cn.qihangerp.open.tao.domain.OmsTaoGoodsSku
*/
public interface OmsTaoGoodsSkuMapper extends BaseMapper<OmsTaoGoodsSku> {
    ErpGoodsSpecVo selectGoodsSpecBySpecNum(String specNum);
    IPage<TaoGoodsSkuListVo> selectSkuPageList(Page<OmsTaoGoodsSku> page, @Param("shopId") Long shopId, @Param("numIid") Long numIid);
}




