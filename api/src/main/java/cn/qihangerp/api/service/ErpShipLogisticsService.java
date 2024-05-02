package cn.qihangerp.api.service;

import java.util.List;
import cn.qihangerp.api.domain.ErpShipLogistics;

/**
 * 物流公司Service接口
 * 
 * @author qihang
 * @date 2024-01-12
 */
public interface ErpShipLogisticsService
{
    /**
     * 查询物流公司
     * 
     * @param id 物流公司主键
     * @return 物流公司
     */
    public ErpShipLogistics selectBLogisticsCompanyById(Long id);

    /**
     * 查询物流公司列表
     * 
     * @param bLogisticsCompany 物流公司
     * @return 物流公司集合
     */
    public List<ErpShipLogistics> selectBLogisticsCompanyList(ErpShipLogistics bLogisticsCompany);

    /**
     * 新增物流公司
     * 
     * @param bLogisticsCompany 物流公司
     * @return 结果
     */
    public int insertBLogisticsCompany(ErpShipLogistics bLogisticsCompany);

    /**
     * 修改物流公司
     * 
     * @param bLogisticsCompany 物流公司
     * @return 结果
     */
    public int updateBLogisticsCompany(ErpShipLogistics bLogisticsCompany);

    /**
     * 批量删除物流公司
     * 
     * @param ids 需要删除的物流公司主键集合
     * @return 结果
     */
    public int deleteBLogisticsCompanyByIds(Long[] ids);

    /**
     * 删除物流公司信息
     * 
     * @param id 物流公司主键
     * @return 结果
     */
    public int deleteBLogisticsCompanyById(Long id);
}
