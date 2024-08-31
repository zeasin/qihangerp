package cn.qihangerp.open.tao.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.open.tao.domain.OmsTaoGoodsSku;
import cn.qihangerp.open.tao.domain.vo.ErpGoodsSpecVo;
import cn.qihangerp.open.tao.mapper.OmsTaoGoodsSkuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.open.tao.domain.OmsTaoGoods;
import cn.qihangerp.open.tao.service.OmsTaoGoodsService;
import cn.qihangerp.open.tao.mapper.OmsTaoGoodsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【oms_tao_goods(淘宝商品表)】的数据库操作Service实现
* @createDate 2024-04-29 19:56:59
*/
@AllArgsConstructor
@Service
public class OmsTaoGoodsServiceImpl extends ServiceImpl<OmsTaoGoodsMapper, OmsTaoGoods>
    implements OmsTaoGoodsService{
    private final OmsTaoGoodsMapper mapper;
    private final OmsTaoGoodsSkuMapper skuMapper;
//    private final OGoodsSkuMapper goodsSkuMapper;

    @Override
    public PageResult<OmsTaoGoods> queryPageList(OmsTaoGoods bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsTaoGoods> queryWrapper = new LambdaQueryWrapper<OmsTaoGoods>()
                .eq(bo.getShopId()!=null,OmsTaoGoods::getShopId,bo.getShopId());

        Page<OmsTaoGoods> taoGoodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(taoGoodsPage);
    }

    @Transactional
    @Override
    public int saveAndUpdateGoods(Long shopId, OmsTaoGoods goods) {
        List<OmsTaoGoods> goodsList = mapper.selectList(new LambdaQueryWrapper<OmsTaoGoods>().eq(OmsTaoGoods::getNumIid, goods.getNumIid()));
        if(goodsList!=null && goodsList.size()>0){
            // 存在，更新
            goods.setShopId(shopId);
            goods.setId(goodsList.get(0).getId());
            mapper.updateById(goods);

            // 删除sku
            skuMapper.delete(new LambdaQueryWrapper<OmsTaoGoodsSku>().eq(OmsTaoGoodsSku::getNumIid,goods.getNumIid()));

            // 重新插入sku
            if(goods.getSkuList()!=null) {
                for (var sku : goods.getSkuList()) {
//                    sku.setTaoGoodsId(goods.getId());
                    sku.setShopId(shopId);
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
//                    sku.setTaoGoodsId(goods.getId());
                    sku.setShopId(shopId);
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
}




