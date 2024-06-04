package cn.qihangerp.api.wei.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.domain.ShopSetting;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.wei.domain.OmsWeiGoodsSku;
import cn.qihangerp.api.wei.service.OmsWeiGoodsSkuService;
import cn.qihangerp.api.wei.mapper.OmsWeiGoodsSkuMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author TW
* @description 针对表【oms_wei_goods_sku】的数据库操作Service实现
* @createDate 2024-05-06 16:01:18
*/
@AllArgsConstructor
@Service
public class OmsWeiGoodsSkuServiceImpl extends ServiceImpl<OmsWeiGoodsSkuMapper, OmsWeiGoodsSku>
    implements OmsWeiGoodsSkuService{
    private final  OmsWeiGoodsSkuMapper mapper;

    @Override
    public PageResult<OmsWeiGoodsSku> queryPageList(OmsWeiGoodsSku bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsWeiGoodsSku> queryWrapper = new LambdaQueryWrapper<OmsWeiGoodsSku>()
                .eq(bo.getShopId()!=null,OmsWeiGoodsSku::getShopId,bo.getShopId())
                ;

        Page<OmsWeiGoodsSku> page = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(page);
    }

    @Override
    public Shop selectShopById(Long id) {
        return mapper.selectShopById(id);
    }

    @Override
    public ShopSetting selectShopSettingById(Integer id) {
        return mapper.selectShopSettingById(id.longValue());
    }

    @Override
    public void updateShopSessionByShopId(Long shopId, String sessionKey) {
        mapper.updateShopSessionByShopId(shopId,sessionKey);
    }
}




