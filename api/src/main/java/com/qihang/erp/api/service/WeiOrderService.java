package com.qihang.erp.api.service;

import com.qihang.common.PageQuery;
import com.qihang.common.PageResult;
import com.qihang.erp.api.common.ResultVo;
import com.qihang.erp.api.domain.WeiOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【wei_order】的数据库操作Service
* @createDate 2024-04-15 17:28:27
*/
public interface WeiOrderService extends IService<WeiOrder> {
    PageResult<WeiOrder> queryPageList(WeiOrder bo, PageQuery pageQuery);
    ResultVo<Integer> saveOrder(Integer shopId, WeiOrder order);
    ResultVo<Integer> orderConfirm(String[] ids);
}
