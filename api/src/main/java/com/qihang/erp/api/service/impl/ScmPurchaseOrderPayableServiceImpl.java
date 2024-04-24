package com.qihang.erp.api.service.impl;

import java.util.List;
import com.qihang.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.ScmPurchaseOrderPayableMapper;
import com.qihang.erp.api.service.IScmPurchaseOrderPayableService;

/**
 * 财务管理-应付款-采购货款Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Service
public class ScmPurchaseOrderPayableServiceImpl implements IScmPurchaseOrderPayableService
{
    @Autowired
    private ScmPurchaseOrderPayableMapper fmsPayablePurchaseMapper;

    /**
     * 查询财务管理-应付款-采购货款
     * 
     * @param id 财务管理-应付款-采购货款主键
     * @return 财务管理-应付款-采购货款
     */
    @Override
    public com.qihang.erp.api.domain.ScmPurchaseOrderPayable selectFmsPayablePurchaseById(Long id)
    {
        return fmsPayablePurchaseMapper.selectFmsPayablePurchaseById(id);
    }

    /**
     * 查询财务管理-应付款-采购货款列表
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 财务管理-应付款-采购货款
     */
    @Override
    public List<com.qihang.erp.api.domain.ScmPurchaseOrderPayable> selectFmsPayablePurchaseList(com.qihang.erp.api.domain.ScmPurchaseOrderPayable fmsPayablePurchase)
    {
        return fmsPayablePurchaseMapper.selectFmsPayablePurchaseList(fmsPayablePurchase);
    }

    /**
     * 新增财务管理-应付款-采购货款
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 结果
     */
    @Override
    public int insertFmsPayablePurchase(com.qihang.erp.api.domain.ScmPurchaseOrderPayable fmsPayablePurchase)
    {
        fmsPayablePurchase.setCreateTime(DateUtils.getNowDate());
        return fmsPayablePurchaseMapper.insertFmsPayablePurchase(fmsPayablePurchase);
    }

    /**
     * 修改财务管理-应付款-采购货款
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 结果
     */
    @Override
    public int updateFmsPayablePurchase(com.qihang.erp.api.domain.ScmPurchaseOrderPayable fmsPayablePurchase)
    {
        fmsPayablePurchase.setUpdateTime(DateUtils.getNowDate());
        return fmsPayablePurchaseMapper.updateFmsPayablePurchase(fmsPayablePurchase);
    }

    /**
     * 批量删除财务管理-应付款-采购货款
     * 
     * @param ids 需要删除的财务管理-应付款-采购货款主键
     * @return 结果
     */
    @Override
    public int deleteFmsPayablePurchaseByIds(Long[] ids)
    {
        return fmsPayablePurchaseMapper.deleteFmsPayablePurchaseByIds(ids);
    }

    /**
     * 删除财务管理-应付款-采购货款信息
     * 
     * @param id 财务管理-应付款-采购货款主键
     * @return 结果
     */
    @Override
    public int deleteFmsPayablePurchaseById(Long id)
    {
        return fmsPayablePurchaseMapper.deleteFmsPayablePurchaseById(id);
    }
}
