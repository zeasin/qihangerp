package cn.qihangerp.api.service;

import cn.qihangerp.api.domain.bo.RefundBo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.ErpSaleAfterRefund;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【erp_sale_after_refund(售后退款表)】的数据库操作Service
* @createDate 2024-05-04 19:53:00
*/
public interface ErpSaleAfterRefundService extends IService<ErpSaleAfterRefund> {
    ResultVo<Integer> taoRefundMessage(ErpSaleAfterRefund refund);
    PageResult<ErpSaleAfterRefund> queryPageList(RefundBo bo, PageQuery pageQuery);

}
