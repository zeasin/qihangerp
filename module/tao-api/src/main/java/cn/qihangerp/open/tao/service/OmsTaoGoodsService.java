package cn.qihangerp.open.tao.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.open.tao.domain.OmsTaoGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【oms_tao_goods(淘宝商品表)】的数据库操作Service
* @createDate 2024-04-29 19:56:59
*/
public interface OmsTaoGoodsService extends IService<OmsTaoGoods> {
    PageResult<OmsTaoGoods> queryPageList(OmsTaoGoods bo, PageQuery pageQuery);

    int saveAndUpdateGoods(Long shopId,OmsTaoGoods goods);
}
