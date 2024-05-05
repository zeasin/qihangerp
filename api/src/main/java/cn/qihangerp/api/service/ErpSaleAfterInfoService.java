package cn.qihangerp.api.service;

import cn.qihangerp.api.domain.ErpSaleAfterInfo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【erp_sale_after_info(售后处理表)】的数据库操作Service
* @createDate 2024-05-05 20:16:21
*/
public interface ErpSaleAfterInfoService extends IService<ErpSaleAfterInfo> {
    PageResult<ErpSaleAfterInfo> queryPageList(ErpSaleAfterInfo bo, PageQuery pageQuery);
}
