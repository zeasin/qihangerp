package cn.qihangerp.api.pdd.service.impl;

import cn.qihangerp.api.pdd.domain.OmsPddGoodsSku;
import cn.qihangerp.api.pdd.mapper.OmsPddGoodsSkuMapper;
import cn.qihangerp.api.pdd.vo.ErpGoodsSpecVo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.pdd.domain.OmsPddGoods;
import cn.qihangerp.api.pdd.service.OmsPddGoodsService;
import cn.qihangerp.api.pdd.mapper.OmsPddGoodsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【oms_pdd_goods(pdd商品表)】的数据库操作Service实现
* @createDate 2024-05-28 09:27:08
*/
@AllArgsConstructor
@Service
public class OmsPddGoodsServiceImpl extends ServiceImpl<OmsPddGoodsMapper, OmsPddGoods>
    implements OmsPddGoodsService{
    private final OmsPddGoodsMapper mapper;
    private final OmsPddGoodsSkuMapper skuMapper;
    @Override
    public PageResult<OmsPddGoods> queryPageList(OmsPddGoods bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsPddGoods> queryWrapper = new LambdaQueryWrapper<OmsPddGoods>()
                .eq(bo.getShopId()!=null,OmsPddGoods::getShopId,bo.getShopId());

        Page<OmsPddGoods> goodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(goodsPage);
    }

    @Override
    public int saveAndUpdateGoods(Long shopId, OmsPddGoods goods) {
        List<OmsPddGoods> goodsList = mapper.selectList(new LambdaQueryWrapper<OmsPddGoods>().eq(OmsPddGoods::getGoodsId, goods.getGoodsId()));
        if(goodsList!=null && goodsList.size()>0){
            // 存在，更新
            goods.setShopId(shopId);
            goods.setId(goodsList.get(0).getId());
            goods.setUpdateTime(new Date());
            mapper.updateById(goods);

            // 删除sku
            skuMapper.delete(new LambdaQueryWrapper<OmsPddGoodsSku>().eq(OmsPddGoodsSku::getGoodsId,goods.getGoodsId()));

            // 重新插入sku
            if(goods.getSkuList()!=null) {
                for (var sku : goods.getSkuList()) {
                    sku.setShopId(shopId);
                    sku.setGoodsId(goods.getGoodsId());
                    sku.setThumbUrl(goods.getThumbUrl());
                    sku.setGoodsName(goods.getGoodsName());
                    // 根据OuterId查找ERP系统中的skuid
                    if(StringUtils.isNotEmpty(sku.getOuterId())) {
                        ErpGoodsSpecVo erpGoodsSpecVo = skuMapper.selectGoodsSpecBySpecNum(sku.getOuterId());
                        if(erpGoodsSpecVo!=null ){
                            sku.setErpGoodsId(erpGoodsSpecVo.getGoodsId());
                            sku.setErpGoodsSkuId(erpGoodsSpecVo.getId());
//                            sku.setErpSupplierId(erpGoodsSpecVo.getSupplierId());
                        }
                    }
                    skuMapper.insert(sku);
                }
            }
            return ResultVoEnum.DataExist.getIndex();
        }else {
            // 不存在，新增
            goods.setShopId(shopId);
            goods.setCreateTime(new Date());
            mapper.insert(goods);
            // 插入sku
            if(goods.getSkuList()!=null) {
                for (var sku : goods.getSkuList()) {
                    sku.setShopId(shopId);
                    sku.setGoodsId(goods.getGoodsId());
                    sku.setThumbUrl(goods.getThumbUrl());
                    sku.setGoodsName(goods.getGoodsName());
                    // 根据OuterId查找ERP系统中的skuid
                    if(StringUtils.isNotEmpty(sku.getOuterId())) {
                        ErpGoodsSpecVo erpGoodsSpecVo = skuMapper.selectGoodsSpecBySpecNum(sku.getOuterId());
                        if(erpGoodsSpecVo!=null ){
                            sku.setErpGoodsId(erpGoodsSpecVo.getGoodsId());
                            sku.setErpGoodsSkuId(erpGoodsSpecVo.getId());
//                            sku.setErpSupplierId(erpGoodsSpecVo.getSupplierId());
                        }
                    }
                    skuMapper.insert(sku);
                }
            }
            return 0;
        }
    }

    @Override
    public Shop selectShopById(Long id) {
        return mapper.selectShopById(id);
    }

    @Override
    public ShopSetting selectShopSettingById(Integer id) {
        return mapper.selectShopSettingById(id);
    }

    @Override
    public void updateShopSessionByShopId(Long shopId, String accessToken) {
        mapper.updateShopSessionByShopId(shopId,accessToken);
    }
}




