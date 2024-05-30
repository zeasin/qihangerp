package cn.qihangerp.api.dou.service.impl;

import cn.qihangerp.api.dou.domain.OmsDouGoodsSku;
import cn.qihangerp.api.dou.domain.vo.ErpGoodsSpecVo;
import cn.qihangerp.api.dou.mapper.OmsDouGoodsSkuMapper;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.dou.domain.OmsDouGoods;
import cn.qihangerp.api.dou.service.OmsDouGoodsService;
import cn.qihangerp.api.dou.mapper.OmsDouGoodsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【oms_dou_goods(抖店商品表)】的数据库操作Service实现
* @createDate 2024-05-28 20:35:45
*/
@AllArgsConstructor
@Service
public class OmsDouGoodsServiceImpl extends ServiceImpl<OmsDouGoodsMapper, OmsDouGoods>
    implements OmsDouGoodsService{
    private final OmsDouGoodsMapper mapper;
    private final OmsDouGoodsSkuMapper skuMapper;
    @Override
    public PageResult<OmsDouGoods> queryPageList(OmsDouGoods bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsDouGoods> queryWrapper = new LambdaQueryWrapper<OmsDouGoods>()
                .eq(bo.getShopId()!=null,OmsDouGoods::getShopId,bo.getShopId());

        Page<OmsDouGoods> goodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(goodsPage);
    }

    @Override
    public int saveAndUpdateGoods(Long shopId, OmsDouGoods goods) {
        List<OmsDouGoods> goodsList = mapper.selectList(new LambdaQueryWrapper<OmsDouGoods>().eq(OmsDouGoods::getProductId, goods.getProductId()));
        if(goodsList!=null && goodsList.size()>0){
            // 存在，更新
            goods.setShopId(shopId);
            goods.setId(goodsList.get(0).getId());
            goods.setModifyTime(new Date());
            mapper.updateById(goods);

            // 删除sku
            skuMapper.delete(new LambdaQueryWrapper<OmsDouGoodsSku>().eq(OmsDouGoodsSku::getProductId,goods.getProductId()));

            // 重新插入sku
            if(goods.getSkuList()!=null) {
                for (var sku : goods.getSkuList()) {
                    sku.setShopId(shopId);
                    sku.setImg(goods.getImg());
                    sku.setName(goods.getName());
                    sku.setErpGoodsId(0L);
                    sku.setErpGoodsSkuId(0L);
                    // 根据OuterId查找ERP系统中的skuid
                    if(StringUtils.isNotEmpty(sku.getCode())) {
                        ErpGoodsSpecVo erpGoodsSpecVo = skuMapper.selectGoodsSpecBySpecNum(sku.getCode());
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
            goods.setPullTime(new Date());
            mapper.insert(goods);
            // 插入sku
            if(goods.getSkuList()!=null) {
                for (var sku : goods.getSkuList()) {
                    sku.setShopId(shopId);
                    sku.setImg(goods.getImg());
                    sku.setName(goods.getName());
                    sku.setErpGoodsId(0L);
                    sku.setErpGoodsSkuId(0L);
                    // 根据OuterId查找ERP系统中的skuid
                    if(StringUtils.isNotEmpty(sku.getCode())) {
                        ErpGoodsSpecVo erpGoodsSpecVo = skuMapper.selectGoodsSpecBySpecNum(sku.getCode());
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
    public ShopSetting selectShopSettingById(Long id) {
        return mapper.selectShopSettingById(id);
    }

    @Override
    public void updateShopSessionByShopId(Long shopId, String sessionKey) {
        mapper.updateShopSessionByShopId(shopId,sessionKey);
    }
}




