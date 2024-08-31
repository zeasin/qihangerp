package cn.qihangerp.api.wei.service;

import cn.qihangerp.api.wei.bo.WeiOrderConfirmBo;
import cn.qihangerp.api.wei.domain.OmsWeiOrder;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_wei_order】的数据库操作Service
* @createDate 2024-05-06 19:23:39
*/
public interface OmsWeiOrderService extends IService<OmsWeiOrder> {
    PageResult<OmsWeiOrder> queryPageList(OmsWeiOrder bo, PageQuery pageQuery);
    ResultVo<Integer> saveOrder(Long shopId, OmsWeiOrder order);
    OmsWeiOrder queryDetailById(Long id);
    int confirmOrder(WeiOrderConfirmBo bo) throws InterruptedException;
}
