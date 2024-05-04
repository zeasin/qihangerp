package cn.qihangerp.open.tao.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.open.tao.domain.bo.TaoRefundBo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.open.tao.domain.OmsTaoRefund;
import cn.qihangerp.open.tao.service.OmsTaoRefundService;
import cn.qihangerp.open.tao.mapper.OmsTaoRefundMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【oms_tao_refund(淘宝退款表)】的数据库操作Service实现
* @createDate 2024-04-30 13:52:20
*/
@AllArgsConstructor
@Service
public class OmsTaoRefundServiceImpl extends ServiceImpl<OmsTaoRefundMapper, OmsTaoRefund>
    implements OmsTaoRefundService{
    private final OmsTaoRefundMapper mapper;

    @Override
    public PageResult<OmsTaoRefund> queryPageList(TaoRefundBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OmsTaoRefund> queryWrapper = new LambdaQueryWrapper<OmsTaoRefund>()
                .eq(bo.getShopId()!=null,OmsTaoRefund::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getRefundId()),OmsTaoRefund::getRefundId,bo.getRefundId())
                .eq(StringUtils.hasText(bo.getTid()),OmsTaoRefund::getTid,bo.getTid())
                ;

        Page<OmsTaoRefund> page = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(page);
    }

    @Override
    public ResultVo<Integer>  saveAndUpdateRefund(Long shopId, OmsTaoRefund refund) {
        List<OmsTaoRefund> taoRefunds = mapper.selectList(new LambdaQueryWrapper<OmsTaoRefund>().eq(OmsTaoRefund::getRefundId, refund.getRefundId()));
        if(taoRefunds!=null && taoRefunds.size()>0){
            // 存在，修改
            OmsTaoRefund update = new OmsTaoRefund();
            update.setId(taoRefunds.get(0).getId());
            update.setTid(refund.getTid());
            update.setOid(refund.getOid());
            update.setNumIid(refund.getNumIid());
            update.setOuterId(refund.getOuterId());
            update.setModified(refund.getModified());
            update.setStatus(refund.getStatus());
            update.setEndTime(refund.getEndTime());
            update.setUpdateTime(new Date());
            mapper.updateById(update);
            return ResultVo.success("更新成功");
        }else{
            refund.setShopId(shopId);
            refund.setCreateTime(new Date());
            mapper.insert(refund);
            return  ResultVo.success("新增成功");
        }

    }
}




