package cn.qihangerp.api.jd.service;

import cn.qihangerp.api.jd.domain.OmsJdAfterSale;
import cn.qihangerp.api.jd.domain.bo.JdAfterSaleBo;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_jd_after_sale(京东退款与售后表)】的数据库操作Service
* @createDate 2024-06-11 18:32:21
*/
public interface OmsJdAfterSaleService extends IService<OmsJdAfterSale> {
    PageResult<OmsJdAfterSale> queryPageList(JdAfterSaleBo bo, PageQuery pageQuery);
    ResultVo<Integer> saveAfter(Long shopId, OmsJdAfterSale after);
}
