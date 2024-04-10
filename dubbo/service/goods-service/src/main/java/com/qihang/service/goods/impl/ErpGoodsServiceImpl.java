package com.qihang.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.interfaces.goods.domain.ErpGoods;
import com.qihang.interfaces.goods.service.ErpGoodsService;
import com.qihang.service.goods.mapper.ErpGoodsMapper;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
* @author TW
* @description 针对表【erp_goods(商品库存管理)】的数据库操作Service实现
* @createDate 2024-03-26 10:06:36
*/
@DubboService
@AllArgsConstructor
@Service
public class ErpGoodsServiceImpl extends ServiceImpl<ErpGoodsMapper, ErpGoods>
    implements ErpGoodsService{
    private final ErpGoodsMapper mapper;
    @Override
    public PageResult<ErpGoods> queryPageList(ErpGoods goods, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpGoods> queryWrapper = new LambdaQueryWrapper<ErpGoods>();
        Page<ErpGoods> pages = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(pages);
    }
}




