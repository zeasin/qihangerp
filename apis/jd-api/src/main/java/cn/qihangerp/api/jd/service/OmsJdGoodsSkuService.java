package cn.qihangerp.api.jd.service;

import cn.qihangerp.api.jd.bo.JdOrderBo;
import cn.qihangerp.api.jd.domain.OmsJdGoodsSku;
import cn.qihangerp.api.jd.domain.OmsJdOrder;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【oms_jd_goods_sku(京东商品SKU表)】的数据库操作Service
* @createDate 2024-05-03 18:09:33
*/
public interface OmsJdGoodsSkuService extends IService<OmsJdGoodsSku> {
    PageResult<OmsJdGoodsSku> queryPageList(OmsJdGoodsSku bo, PageQuery pageQuery);

    ResultVo<Integer> saveGoodsSku(Long shopId, OmsJdGoodsSku goodsSku);
}
