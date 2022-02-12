package io.github.leopard.system.mapper;

import java.util.List;
import io.github.leopard.system.domain.BizOrder;

/**
 * 订单信息Mapper接口
 * 
 * @author admin
 * @date 2022-02-12
 */
public interface BizOrderMapper 
{
    /**
     * 查询订单信息
     * 
     * @param id 订单信息主键
     * @return 订单信息
     */
    public BizOrder selectBizOrderById(String id);

    /**
     * 查询订单信息列表
     * 
     * @param bizOrder 订单信息
     * @return 订单信息集合
     */
    public List<BizOrder> selectBizOrderList(BizOrder bizOrder);

    /**
     * 新增订单信息
     * 
     * @param bizOrder 订单信息
     * @return 结果
     */
    public int insertBizOrder(BizOrder bizOrder);

    /**
     * 修改订单信息
     * 
     * @param bizOrder 订单信息
     * @return 结果
     */
    public int updateBizOrder(BizOrder bizOrder);

    /**
     * 删除订单信息
     * 
     * @param id 订单信息主键
     * @return 结果
     */
    public int deleteBizOrderById(String id);

    /**
     * 批量删除订单信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizOrderByIds(String[] ids);
}
