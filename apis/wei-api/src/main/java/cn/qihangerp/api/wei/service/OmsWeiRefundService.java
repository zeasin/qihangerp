package cn.qihangerp.api.wei.service;

import cn.qihangerp.api.wei.domain.OmsWeiRefund;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_wei_refund(视频号小店退款)】的数据库操作Service
* @createDate 2024-05-07 14:31:54
*/
public interface OmsWeiRefundService extends IService<OmsWeiRefund> {
    PageResult<OmsWeiRefund> queryPageList(OmsWeiRefund bo, PageQuery pageQuery);
    ResultVo<Integer> saveRefund(Long shopId, OmsWeiRefund refund);
}
