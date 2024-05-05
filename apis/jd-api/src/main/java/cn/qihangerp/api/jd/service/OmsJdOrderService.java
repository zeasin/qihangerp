package cn.qihangerp.api.jd.service;

import cn.qihangerp.api.jd.bo.JdOrderBo;
import cn.qihangerp.api.jd.bo.JdOrderConfirmBo;
import cn.qihangerp.api.jd.domain.OmsJdOrder;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【oms_jd_order(京东订单表)】的数据库操作Service
* @createDate 2024-05-03 12:21:08
*/
public interface OmsJdOrderService extends IService<OmsJdOrder> {
    PageResult<OmsJdOrder> queryPageList(JdOrderBo bo, PageQuery pageQuery);
    ResultVo<Integer> saveOrder(Long shopId, OmsJdOrder order);
    OmsJdOrder queryDetailById(Long id);

    int confirmOrder(JdOrderConfirmBo taoOrder) throws InterruptedException;
}
