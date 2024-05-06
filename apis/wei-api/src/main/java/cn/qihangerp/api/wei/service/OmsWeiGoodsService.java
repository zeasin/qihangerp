package cn.qihangerp.api.wei.service;

import cn.qihangerp.api.wei.domain.OmsWeiGoods;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_wei_goods】的数据库操作Service
* @createDate 2024-05-06 16:01:18
*/
public interface OmsWeiGoodsService extends IService<OmsWeiGoods> {
    PageResult<OmsWeiGoods> queryPageList(OmsWeiGoods bo, PageQuery pageQuery);
    int saveAndUpdateGoods(Long shopId,OmsWeiGoods goods);
}
