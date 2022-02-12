package io.github.leopard.system.service.impl;

import java.util.List;
import io.github.leopard.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.leopard.system.mapper.BizOrderMapper;
import io.github.leopard.system.domain.BizOrder;
import io.github.leopard.system.service.IBizOrderService;
import io.github.leopard.common.core.text.Convert;

/**
 * 订单信息Service业务层处理
 * 
 * @author admin
 * @date 2022-02-12
 */
@Service
public class BizOrderServiceImpl implements IBizOrderService 
{
    @Autowired
    private BizOrderMapper bizOrderMapper;

    /**
     * 查询订单信息
     * 
     * @param id 订单信息主键
     * @return 订单信息
     */
    @Override
    public BizOrder selectBizOrderById(String id)
    {
        return bizOrderMapper.selectBizOrderById(id);
    }

    /**
     * 查询订单信息列表
     * 
     * @param bizOrder 订单信息
     * @return 订单信息
     */
    @Override
    public List<BizOrder> selectBizOrderList(BizOrder bizOrder)
    {
        return bizOrderMapper.selectBizOrderList(bizOrder);
    }

    /**
     * 新增订单信息
     * 
     * @param bizOrder 订单信息
     * @return 结果
     */
    @Override
    public int insertBizOrder(BizOrder bizOrder)
    {
        bizOrder.setCreateTime(DateUtils.getNowDate());
        return bizOrderMapper.insertBizOrder(bizOrder);
    }

    /**
     * 修改订单信息
     * 
     * @param bizOrder 订单信息
     * @return 结果
     */
    @Override
    public int updateBizOrder(BizOrder bizOrder)
    {
        bizOrder.setUpdateTime(DateUtils.getNowDate());
        return bizOrderMapper.updateBizOrder(bizOrder);
    }

    /**
     * 批量删除订单信息
     * 
     * @param ids 需要删除的订单信息主键
     * @return 结果
     */
    @Override
    public int deleteBizOrderByIds(String ids)
    {
        return bizOrderMapper.deleteBizOrderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除订单信息信息
     * 
     * @param id 订单信息主键
     * @return 结果
     */
    @Override
    public int deleteBizOrderById(String id)
    {
        return bizOrderMapper.deleteBizOrderById(id);
    }
}
