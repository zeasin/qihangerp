package cn.qihangerp.api.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.api.domain.ErpSaleAfterInfo;
import cn.qihangerp.api.service.ErpSaleAfterInfoService;
import cn.qihangerp.api.mapper.ErpSaleAfterInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author qilip
* @description 针对表【erp_sale_after_info(售后处理表)】的数据库操作Service实现
* @createDate 2024-05-05 20:16:21
*/
@AllArgsConstructor
@Service
public class ErpSaleAfterInfoServiceImpl extends ServiceImpl<ErpSaleAfterInfoMapper, ErpSaleAfterInfo>
    implements ErpSaleAfterInfoService{
    private final ErpSaleAfterInfoMapper mapper;
    @Override
    public PageResult<ErpSaleAfterInfo> queryPageList(ErpSaleAfterInfo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpSaleAfterInfo> queryWrapper = new LambdaQueryWrapper<ErpSaleAfterInfo>().
                eq( ErpSaleAfterInfo::getType, bo.getType())
                .eq(bo.getShopId() != null, ErpSaleAfterInfo::getShopId, bo.getShopId());

        Page<ErpSaleAfterInfo> pages = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(pages);
    }
}




