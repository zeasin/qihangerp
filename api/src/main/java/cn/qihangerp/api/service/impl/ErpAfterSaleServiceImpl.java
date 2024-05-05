//package cn.qihangerp.api.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import cn.qihangerp.common.PageQuery;
//import cn.qihangerp.common.PageResult;
//import cn.qihangerp.api.domain.ErpAfterSale;
//import cn.qihangerp.api.service.ErpAfterSaleService;
//import cn.qihangerp.api.mapper.ErpAfterSaleMapper;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
///**
//* @author TW
//* @description 针对表【erp_after_sale(售后处理表)】的数据库操作Service实现
//* @createDate 2024-04-15 14:30:34
//*/
//@AllArgsConstructor
//@Service
//public class ErpAfterSaleServiceImpl extends ServiceImpl<ErpAfterSaleMapper, ErpAfterSale>
//    implements ErpAfterSaleService{
//    private final ErpAfterSaleMapper mapper;
//    @Override
//    public PageResult<ErpAfterSale> queryPageList(ErpAfterSale bo, PageQuery pageQuery) {
//        LambdaQueryWrapper<ErpAfterSale> queryWrapper = new LambdaQueryWrapper<ErpAfterSale>().
//                eq( ErpAfterSale::getType, bo.getType())
//                .eq(bo.getShopId() != null, ErpAfterSale::getShopId, bo.getShopId());
//
//        Page<ErpAfterSale> pages = mapper.selectPage(pageQuery.build(), queryWrapper);
//        return PageResult.build(pages);
//    }
//}
//
//
//
//
