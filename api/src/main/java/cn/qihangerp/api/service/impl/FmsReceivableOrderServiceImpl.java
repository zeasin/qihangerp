//package cn.qihangerp.api.service.impl;
//
//import java.util.List;
//import com.qihang.common.utils.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import cn.qihangerp.api.mapper.FmsReceivableOrderMapper;
//import cn.qihangerp.api.domain.FmsReceivableOrder;
//import cn.qihangerp.api.service.IFmsReceivableOrderService;
//
///**
// * 财务管理-应收款-订单收入Service业务层处理
// *
// * @author qihang
// * @date 2024-01-28
// */
//@Service
//public class FmsReceivableOrderServiceImpl implements IFmsReceivableOrderService
//{
//    @Autowired
//    private FmsReceivableOrderMapper fmsReceivableOrderMapper;
//
//    /**
//     * 查询财务管理-应收款-订单收入
//     *
//     * @param id 财务管理-应收款-订单收入主键
//     * @return 财务管理-应收款-订单收入
//     */
//    @Override
//    public FmsReceivableOrder selectFmsReceivableOrderById(Long id)
//    {
//        return fmsReceivableOrderMapper.selectFmsReceivableOrderById(id);
//    }
//
//    /**
//     * 查询财务管理-应收款-订单收入列表
//     *
//     * @param fmsReceivableOrder 财务管理-应收款-订单收入
//     * @return 财务管理-应收款-订单收入
//     */
//    @Override
//    public List<FmsReceivableOrder> selectFmsReceivableOrderList(FmsReceivableOrder fmsReceivableOrder)
//    {
//        return fmsReceivableOrderMapper.selectFmsReceivableOrderList(fmsReceivableOrder);
//    }
//
//    /**
//     * 新增财务管理-应收款-订单收入
//     *
//     * @param fmsReceivableOrder 财务管理-应收款-订单收入
//     * @return 结果
//     */
//    @Override
//    public int insertFmsReceivableOrder(FmsReceivableOrder fmsReceivableOrder)
//    {
//        fmsReceivableOrder.setCreateTime(DateUtils.getNowDate());
//        return fmsReceivableOrderMapper.insertFmsReceivableOrder(fmsReceivableOrder);
//    }
//
//    /**
//     * 修改财务管理-应收款-订单收入
//     *
//     * @param fmsReceivableOrder 财务管理-应收款-订单收入
//     * @return 结果
//     */
//    @Override
//    public int updateFmsReceivableOrder(FmsReceivableOrder fmsReceivableOrder)
//    {
//        fmsReceivableOrder.setUpdateTime(DateUtils.getNowDate());
//        return fmsReceivableOrderMapper.updateFmsReceivableOrder(fmsReceivableOrder);
//    }
//
//    /**
//     * 批量删除财务管理-应收款-订单收入
//     *
//     * @param ids 需要删除的财务管理-应收款-订单收入主键
//     * @return 结果
//     */
//    @Override
//    public int deleteFmsReceivableOrderByIds(Long[] ids)
//    {
//        return fmsReceivableOrderMapper.deleteFmsReceivableOrderByIds(ids);
//    }
//
//    /**
//     * 删除财务管理-应收款-订单收入信息
//     *
//     * @param id 财务管理-应收款-订单收入主键
//     * @return 结果
//     */
//    @Override
//    public int deleteFmsReceivableOrderById(Long id)
//    {
//        return fmsReceivableOrderMapper.deleteFmsReceivableOrderById(id);
//    }
//}
