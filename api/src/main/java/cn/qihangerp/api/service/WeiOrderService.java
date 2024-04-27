package cn.qihangerp.api.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.api.domain.WeiOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【wei_order】的数据库操作Service
* @createDate 2024-04-15 17:28:27
*/
public interface WeiOrderService extends IService<WeiOrder> {
    PageResult<WeiOrder> queryPageList(WeiOrder bo, PageQuery pageQuery);
    ResultVo<Integer> saveOrder(Integer shopId, WeiOrder order);
//    ResultVo<Integer> orderConfirm(String[] ids);
    WeiOrder queryDetailById(Long id);

    int confirmOrder(WeiOrder bo);
}
