package cn.qihangerp.api.dou.service;

import cn.qihangerp.api.dou.domain.OmsDouOrder;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_dou_order(抖店订单表)】的数据库操作Service
* @createDate 2024-05-30 19:48:15
*/
public interface OmsDouOrderService extends IService<OmsDouOrder> {
    PageResult<OmsDouOrder> queryPageList(OmsDouOrder bo, PageQuery pageQuery);
    ResultVo<Integer> saveOrder(Long shopId, OmsDouOrder order);
    int confirmOrder(OmsDouOrder pddOrder);
}
