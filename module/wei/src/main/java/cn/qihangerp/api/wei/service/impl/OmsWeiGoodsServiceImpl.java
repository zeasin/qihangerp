package cn.qihangerp.api.wei.service.impl;

import cn.qihangerp.api.wei.domain.OmsWeiGoodsSku;
import cn.qihangerp.api.wei.mapper.OmsWeiGoodsSkuMapper;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVoEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.wei.domain.OmsWeiGoods;
import cn.qihangerp.api.wei.service.OmsWeiGoodsService;
import cn.qihangerp.api.wei.mapper.OmsWeiGoodsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author TW
* @description 针对表【oms_wei_goods】的数据库操作Service实现
* @createDate 2024-05-06 16:01:18
*/
@AllArgsConstructor
@Service
public class OmsWeiGoodsServiceImpl extends ServiceImpl<OmsWeiGoodsMapper, OmsWeiGoods>
    implements OmsWeiGoodsService{
    private final OmsWeiGoodsMapper mapper;
    private final OmsWeiGoodsSkuMapper skuMapper;
//    private final OGoodsSkuMapper goodsSkuMapper;

    @Override
    public PageResult<OmsWeiGoods> queryPageList(OmsWeiGoods bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsWeiGoods> queryWrapper = new LambdaQueryWrapper<OmsWeiGoods>()
                .eq(bo.getShopId()!=null,OmsWeiGoods::getShopId,bo.getShopId())
                ;

        Page<OmsWeiGoods> taoGoodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(taoGoodsPage);
    }

    @Override
    public int saveAndUpdateGoods(Long shopId, OmsWeiGoods goods) {
        List<OmsWeiGoods> goodsList = mapper.selectList(new LambdaQueryWrapper<OmsWeiGoods>().eq(OmsWeiGoods::getProductId, goods.getProductId()));
        if (goodsList != null && goodsList.size() > 0) {
            // 更新
            // 存在，更新
            goods.setShopId(shopId);
            goods.setId(goodsList.get(0).getId());
            mapper.updateById(goods);

            // 删除sku
            skuMapper.delete(new LambdaQueryWrapper<OmsWeiGoodsSku>().eq(OmsWeiGoodsSku::getProductId,goods.getProductId()));
            // 重新插入sku
            if(goods.getSkuList()!=null) {
                for (var sku : goods.getSkuList()) {
                    sku.setShopId(goods.getShopId());
                    sku.setThumbImg(goods.getHeadImg());
                    sku.setTitle(goods.getTitle());
//                    sku.setWeiGoodsId(goods.getId());
                    // 根据OuterId查找ERP系统中的skuid
//                    if(StringUtils.isNotEmpty(sku.getSkuCode())) {
//                        List<OGoodsSku> oGoodsSkus = goodsSkuMapper.selectList(new LambdaQueryWrapper<OGoodsSku>().eq(OGoodsSku::getSkuNum, sku.getSkuCode()));
//                        if(oGoodsSkus!=null && !oGoodsSkus.isEmpty()){
//                            sku.setErpGoodsId(oGoodsSkus.get(0).getErpGoodsId());
//                            sku.setErpGoodsSkuId(oGoodsSkus.get(0).getErpSkuId());
//                        }
//                    }
                    skuMapper.insert(sku);
                }
            }

            return ResultVoEnum.DataExist.getIndex();
        } else {
            // 不存在，新增return 0;
            // 不存在，新增
            goods.setShopId(shopId);
            mapper.insert(goods);
            // 插入sku
            if(goods.getSkuList()!=null) {
                for (var sku : goods.getSkuList()) {
                    sku.setShopId(goods.getShopId());
                    sku.setThumbImg(goods.getHeadImg());
                    sku.setTitle(goods.getTitle());
//                    sku.setWeiGoodsId(goods.getId());
                    // 根据OuterId查找ERP系统中的skuid
                    // TODO:未完成
//                    if(StringUtils.isNotEmpty(sku.getSkuCode())) {
//                        List<OGoodsSku> oGoodsSkus = goodsSkuMapper.selectList(new LambdaQueryWrapper<OGoodsSku>().eq(OGoodsSku::getSkuNum, sku.getSkuCode()));
//                        if(oGoodsSkus!=null && !oGoodsSkus.isEmpty()){
//                            sku.setErpGoodsId(oGoodsSkus.get(0).getErpGoodsId());
//                            sku.setErpGoodsSkuId(oGoodsSkus.get(0).getErpSkuId());
//                        }
//                    }
                    skuMapper.insert(sku);
                }
            }
            return 0;
        }
    }
}




