package cn.qihangerp.open.tao.service;

import cn.qihangerp.common.ResultVo;
import cn.qihangerp.open.tao.domain.OmsTaoOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_tao_order(淘宝订单表)】的数据库操作Service
* @createDate 2024-04-30 13:52:20
*/
public interface OmsTaoOrderService extends IService<OmsTaoOrder> {
    /**
     * 保存店铺订单
     * @param shopId
     * @param order
     * @return
     */
    ResultVo<Integer> saveOrder(Long shopId, OmsTaoOrder order);
}
