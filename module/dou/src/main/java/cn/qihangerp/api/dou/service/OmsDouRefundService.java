package cn.qihangerp.api.dou.service;

import cn.qihangerp.api.dou.domain.OmsDouRefund;
import cn.qihangerp.api.dou.domain.bo.DouRefundBo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_dou_refund(抖店退款表)】的数据库操作Service
* @createDate 2024-06-12 14:19:04
*/
public interface OmsDouRefundService extends IService<OmsDouRefund> {
    PageResult<OmsDouRefund> queryPageList(DouRefundBo bo, PageQuery pageQuery);
    ResultVo<Integer> saveRefund(Long shopId, OmsDouRefund refund);
}
