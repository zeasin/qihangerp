package cn.qihangerp.api.pdd.service;

import cn.qihangerp.api.pdd.bo.PddAfterSaleBo;
import cn.qihangerp.api.pdd.domain.OmsPddRefund;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_pdd_refund(拼多多订单退款表)】的数据库操作Service
* @createDate 2024-06-12 13:50:40
*/
public interface OmsPddRefundService extends IService<OmsPddRefund> {
    PageResult<OmsPddRefund> queryPageList(PddAfterSaleBo bo, PageQuery pageQuery);
    ResultVo<Integer> saveRefund(Long shopId, OmsPddRefund refund);
}
