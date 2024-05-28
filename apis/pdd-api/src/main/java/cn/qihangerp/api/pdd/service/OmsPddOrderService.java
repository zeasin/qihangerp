package cn.qihangerp.api.pdd.service;

import cn.qihangerp.api.pdd.domain.OmsPddGoods;
import cn.qihangerp.api.pdd.domain.OmsPddOrder;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_pdd_order(拼多多订单表)】的数据库操作Service
* @createDate 2024-05-28 15:27:42
*/
public interface OmsPddOrderService extends IService<OmsPddOrder> {
    PageResult<OmsPddOrder> queryPageList(OmsPddOrder bo, PageQuery pageQuery);
    ResultVo<Integer> saveOrder(Long shopId, OmsPddOrder order);
    int confirmOrder(OmsPddOrder pddOrder);
}
