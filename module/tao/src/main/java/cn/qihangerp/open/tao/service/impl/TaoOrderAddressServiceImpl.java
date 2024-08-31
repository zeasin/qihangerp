package cn.qihangerp.open.tao.service.impl;

import cn.qihangerp.open.tao.domain.TaoOrderAddress;
import cn.qihangerp.open.tao.mapper.TaoOrderAddressMapper;
import cn.qihangerp.open.tao.service.ITaoOrderAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 淘宝订单地址Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Service
public class TaoOrderAddressServiceImpl implements ITaoOrderAddressService
{
    @Autowired
    private TaoOrderAddressMapper taoOrderAddressMapper;

    /**
     * 查询淘宝订单地址
     * 
     * @param id 淘宝订单地址主键
     * @return 淘宝订单地址
     */
    @Override
    public TaoOrderAddress selectTaoOrderAddressById(Long id)
    {
        return taoOrderAddressMapper.selectTaoOrderAddressById(id);
    }

    /**
     * 查询淘宝订单地址列表
     * 
     * @param taoOrderAddress 淘宝订单地址
     * @return 淘宝订单地址
     */
    @Override
    public List<TaoOrderAddress> selectTaoOrderAddressList(TaoOrderAddress taoOrderAddress)
    {
        return taoOrderAddressMapper.selectTaoOrderAddressList(taoOrderAddress);
    }

    /**
     * 新增淘宝订单地址
     * 
     * @param taoOrderAddress 淘宝订单地址
     * @return 结果
     */
    @Override
    public int insertTaoOrderAddress(TaoOrderAddress taoOrderAddress)
    {
        return taoOrderAddressMapper.insertTaoOrderAddress(taoOrderAddress);
    }

    /**
     * 修改淘宝订单地址
     * 
     * @param taoOrderAddress 淘宝订单地址
     * @return 结果
     */
    @Override
    public int updateTaoOrderAddress(TaoOrderAddress taoOrderAddress)
    {
        return taoOrderAddressMapper.updateTaoOrderAddress(taoOrderAddress);
    }

    /**
     * 批量删除淘宝订单地址
     * 
     * @param ids 需要删除的淘宝订单地址主键
     * @return 结果
     */
    @Override
    public int deleteTaoOrderAddressByIds(Long[] ids)
    {
        return taoOrderAddressMapper.deleteTaoOrderAddressByIds(ids);
    }

    /**
     * 删除淘宝订单地址信息
     * 
     * @param id 淘宝订单地址主键
     * @return 结果
     */
    @Override
    public int deleteTaoOrderAddressById(Long id)
    {
        return taoOrderAddressMapper.deleteTaoOrderAddressById(id);
    }
}
