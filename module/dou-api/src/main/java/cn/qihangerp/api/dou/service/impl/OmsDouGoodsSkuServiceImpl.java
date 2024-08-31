package cn.qihangerp.api.dou.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.dou.domain.OmsDouGoodsSku;
import cn.qihangerp.api.dou.service.OmsDouGoodsSkuService;
import cn.qihangerp.api.dou.mapper.OmsDouGoodsSkuMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author TW
* @description 针对表【oms_dou_goods_sku(抖店商品Sku表)】的数据库操作Service实现
* @createDate 2024-05-28 20:35:45
*/
@AllArgsConstructor
@Service
public class OmsDouGoodsSkuServiceImpl extends ServiceImpl<OmsDouGoodsSkuMapper, OmsDouGoodsSku>
    implements OmsDouGoodsSkuService{
    private final OmsDouGoodsSkuMapper mapper;
    @Override
    public PageResult<OmsDouGoodsSku> queryPageList(OmsDouGoodsSku bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsDouGoodsSku> queryWrapper = new LambdaQueryWrapper<OmsDouGoodsSku>()
                .eq(bo.getShopId()!=null,OmsDouGoodsSku::getShopId,bo.getShopId());

        Page<OmsDouGoodsSku> goodsSkuPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(goodsSkuPage);
    }
}




