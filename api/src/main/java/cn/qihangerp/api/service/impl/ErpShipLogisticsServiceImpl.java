package cn.qihangerp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.qihangerp.api.mapper.ErpShipLogisticsMapper;
import cn.qihangerp.api.domain.ErpShipLogistics;
import cn.qihangerp.api.service.ErpShipLogisticsService;

/**
 * 物流公司Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-12
 */
@Service
public class ErpShipLogisticsServiceImpl implements ErpShipLogisticsService
{
    @Autowired
    private ErpShipLogisticsMapper bLogisticsCompanyMapper;

    /**
     * 查询物流公司
     * 
     * @param id 物流公司主键
     * @return 物流公司
     */
    @Override
    public ErpShipLogistics selectBLogisticsCompanyById(Long id)
    {
        return bLogisticsCompanyMapper.selectBLogisticsCompanyById(id);
    }

    /**
     * 查询物流公司列表
     * 
     * @param bLogisticsCompany 物流公司
     * @return 物流公司
     */
    @Override
    public List<ErpShipLogistics> selectBLogisticsCompanyList(ErpShipLogistics bLogisticsCompany)
    {
        return bLogisticsCompanyMapper.selectBLogisticsCompanyList(bLogisticsCompany);
    }

    /**
     * 新增物流公司
     * 
     * @param bLogisticsCompany 物流公司
     * @return 结果
     */
    @Override
    public int insertBLogisticsCompany(ErpShipLogistics bLogisticsCompany)
    {
        return bLogisticsCompanyMapper.insertBLogisticsCompany(bLogisticsCompany);
    }

    /**
     * 修改物流公司
     * 
     * @param bLogisticsCompany 物流公司
     * @return 结果
     */
    @Override
    public int updateBLogisticsCompany(ErpShipLogistics bLogisticsCompany)
    {
        return bLogisticsCompanyMapper.updateBLogisticsCompany(bLogisticsCompany);
    }

    /**
     * 批量删除物流公司
     * 
     * @param ids 需要删除的物流公司主键
     * @return 结果
     */
    @Override
    public int deleteBLogisticsCompanyByIds(Long[] ids)
    {
        return bLogisticsCompanyMapper.deleteBLogisticsCompanyByIds(ids);
    }

    /**
     * 删除物流公司信息
     * 
     * @param id 物流公司主键
     * @return 结果
     */
    @Override
    public int deleteBLogisticsCompanyById(Long id)
    {
        return bLogisticsCompanyMapper.deleteBLogisticsCompanyById(id);
    }
}
