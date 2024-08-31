package cn.qihangerp.api.jd.service.impl;

import cn.qihangerp.api.jd.domain.vo.ErpGoodsSpecVo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.jd.domain.OmsJdGoodsSku;
import cn.qihangerp.api.jd.service.OmsJdGoodsSkuService;
import cn.qihangerp.api.jd.mapper.OmsJdGoodsSkuMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author qilip
* @description 针对表【oms_jd_goods_sku(京东商品SKU表)】的数据库操作Service实现
* @createDate 2024-05-03 18:09:33
*/
@AllArgsConstructor
@Service
public class OmsJdGoodsSkuServiceImpl extends ServiceImpl<OmsJdGoodsSkuMapper, OmsJdGoodsSku>
    implements OmsJdGoodsSkuService{
    private final OmsJdGoodsSkuMapper mapper;

    @Override
    public PageResult<OmsJdGoodsSku> queryPageList(OmsJdGoodsSku bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsJdGoodsSku> queryWrapper = new LambdaQueryWrapper<OmsJdGoodsSku>()
                .eq(bo.getShopId()!=null,OmsJdGoodsSku::getShopId,bo.getShopId());

        Page<OmsJdGoodsSku> page = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(page);
    }

    @Override
    public ResultVo<Integer> saveGoodsSku(Long shopId, OmsJdGoodsSku goodsSku) {
        List<OmsJdGoodsSku> jdGoodsSkus = mapper.selectList(new LambdaQueryWrapper<OmsJdGoodsSku>().eq(OmsJdGoodsSku::getSkuId, goodsSku.getWareId()));
        if(jdGoodsSkus== null || jdGoodsSkus.isEmpty()){
            // 新增
            if(StringUtils.isNotEmpty(goodsSku.getOuterId())) {
                ErpGoodsSpecVo erpGoodsSpecVo = mapper.selectGoodsSpecBySpecNum(goodsSku.getOuterId());
                if(erpGoodsSpecVo!=null ){
                    goodsSku.setErpGoodsId(erpGoodsSpecVo.getGoodsId());
                    goodsSku.setErpGoodsSkuId(erpGoodsSpecVo.getId());
                }
            }
            goodsSku.setShopId(shopId);
            mapper.insert(goodsSku);
        }else{
            // 修改
            goodsSku.setId(jdGoodsSkus.get(0).getId());
            goodsSku.setShopId(shopId);
            if(StringUtils.isNotEmpty(goodsSku.getOuterId())) {
                ErpGoodsSpecVo erpGoodsSpecVo = mapper.selectGoodsSpecBySpecNum(goodsSku.getOuterId());
                if(erpGoodsSpecVo!=null ){
                    goodsSku.setErpGoodsId(erpGoodsSpecVo.getGoodsId());
                    goodsSku.setErpGoodsSkuId(erpGoodsSpecVo.getId());
                }
            }
            mapper.updateById(goodsSku);
        }
        return ResultVo.success();
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
    public void updateShopSessionByShopId(Long shopId, String sessionKey) {
        mapper.updateShopSessionByShopId(shopId,sessionKey);
    }

}




