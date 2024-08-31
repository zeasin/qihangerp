package cn.qihangerp.api.pdd.service.impl;

import cn.qihangerp.api.pdd.domain.OmsPddGoods;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.pdd.domain.OmsPddGoodsSku;
import cn.qihangerp.api.pdd.service.OmsPddGoodsSkuService;
import cn.qihangerp.api.pdd.mapper.OmsPddGoodsSkuMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author TW
* @description 针对表【oms_pdd_goods_sku(pdd商品SKU表)】的数据库操作Service实现
* @createDate 2024-05-28 09:27:08
*/
@AllArgsConstructor
@Service
public class OmsPddGoodsSkuServiceImpl extends ServiceImpl<OmsPddGoodsSkuMapper, OmsPddGoodsSku>
    implements OmsPddGoodsSkuService{
    private final OmsPddGoodsSkuMapper mapper;

    @Override
    public PageResult<OmsPddGoodsSku> queryPageList(OmsPddGoodsSku bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsPddGoodsSku> queryWrapper = new LambdaQueryWrapper<OmsPddGoodsSku>()
                .eq(bo.getShopId()!=null,OmsPddGoodsSku::getShopId,bo.getShopId());

        Page<OmsPddGoodsSku> goodsSkuPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(goodsSkuPage);
    }
}




