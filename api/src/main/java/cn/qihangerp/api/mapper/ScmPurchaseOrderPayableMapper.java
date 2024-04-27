package cn.qihangerp.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 财务管理-应付款-采购货款Mapper接口
 * 
 * @author qihang
 * @date 2024-01-28
 */
@Mapper
public interface ScmPurchaseOrderPayableMapper
{
    /**
     * 查询财务管理-应付款-采购货款
     * 
     * @param id 财务管理-应付款-采购货款主键
     * @return 财务管理-应付款-采购货款
     */
    public cn.qihangerp.api.domain.ScmPurchaseOrderPayable selectFmsPayablePurchaseById(Long id);

    /**
     * 查询财务管理-应付款-采购货款列表
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 财务管理-应付款-采购货款集合
     */
    public List<cn.qihangerp.api.domain.ScmPurchaseOrderPayable> selectFmsPayablePurchaseList(cn.qihangerp.api.domain.ScmPurchaseOrderPayable fmsPayablePurchase);

    /**
     * 新增财务管理-应付款-采购货款
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 结果
     */
    public int insertFmsPayablePurchase(cn.qihangerp.api.domain.ScmPurchaseOrderPayable fmsPayablePurchase);

    /**
     * 修改财务管理-应付款-采购货款
     * 
     * @param fmsPayablePurchase 财务管理-应付款-采购货款
     * @return 结果
     */
    public int updateFmsPayablePurchase(cn.qihangerp.api.domain.ScmPurchaseOrderPayable fmsPayablePurchase);

    /**
     * 删除财务管理-应付款-采购货款
     * 
     * @param id 财务管理-应付款-采购货款主键
     * @return 结果
     */
    public int deleteFmsPayablePurchaseById(Long id);

    /**
     * 批量删除财务管理-应付款-采购货款
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFmsPayablePurchaseByIds(Long[] ids);
}
